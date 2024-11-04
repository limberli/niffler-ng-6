package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage {
  private final SelenideElement descriptionInput = $("#description");
  private final SelenideElement saveBtn = $("#save");
  private final SelenideElement amountInput = $("#amount");
  private final SelenideElement categoryInput = $("#category");

  @Step("Ввести описание траты: {description}")
  public EditSpendingPage setNewSpendingDescription(String description) {
    descriptionInput.clear();
    descriptionInput.setValue(description);
    return this;
  }

  @Step("Сохранить изменения")
  public void save() {
    saveBtn.click();
  }

  @Step("Ввести название категории: {category}")
  public EditSpendingPage setSpendingCategory(String category) {
    categoryInput.setValue(category);
    return this;
  }

  @Step("Ввести сумму траты: {amount}")
  public EditSpendingPage setSpendingAmount(String amount) {
    amountInput.setValue(amount);
    return this;
  }

}