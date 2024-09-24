package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(
            String username,
            String password,
            String friend,
            String income,
            String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("bee", "12345D", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("duck", "12345D", "dima", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("dima", "12345D", null, "bee", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("artem", "12345", null, null, "barsik"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        //Разобраться с forEach (если будет 2 пользователя?) 59 строчка и разобраться Optional<Queue<StaticUser>> 62 строчка
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .findFirst()  // Находим первый параметр, который аннотирован @UserType
                .map(p -> p.getAnnotation(UserType.class))  // Преобразуем в аннотацию UserType
                .ifPresent(ut -> {  // Если аннотация присутствует, продолжаем выполнение
                    Optional<Queue<StaticUser>> userQueue = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    UserType.Type type = ut.value();
                    while (userQueue.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        userQueue = Optional.ofNullable(getQueueByUserType(type));
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    userQueue.ifPresentOrElse(
                            queue -> {
                                StaticUser user = queue.poll();  // Извлекаем пользователя из очереди
                                if (user != null) {
                                    ((Map<UserType, StaticUser>) context.getStore(NAMESPACE).getOrComputeIfAbsent(
                                            context.getUniqueId(), key -> new HashMap<>())).put(ut, user);
                                } else {
                                    throw new IllegalStateException("Очередь пуста, не удается получить пользователя.");
                                }
                            },
                            () -> {
                                throw new IllegalStateException("Невозможно получить пользователя после 30s.");
                            }
                    );
                });
    }

    private Queue<StaticUser> getQueueByUserType(UserType.Type type) {
        return switch (type) {
            case EMPTY -> EMPTY_USERS;
            case WITH_FRIEND -> WITH_FRIEND_USERS;
            case WITH_INCOME_REQUEST -> WITH_INCOME_REQUEST_USERS;
            case WITH_OUTCOME_REQUEST -> WITH_OUTCOME_REQUEST_USERS;
        };
    }


    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> users = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);
        for (Map.Entry<UserType, StaticUser> e : users.entrySet()) {
            UserType.Type type = e.getKey().value();
            getQueueByUserType(type);
            Queue<StaticUser> queue = getQueueByUserType(type);
            queue.add(e.getValue());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (StaticUser) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .get(parameterContext.findAnnotation(UserType.class).get());
    }
}