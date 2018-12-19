package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class mainPage {

	public mainPage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}

	@FindBy(id = "uscodreading")
	public WebElement currentOdometrReading;

	@FindBy(id = "uspodreading")
	public WebElement previousOdometrReading;

	@FindBy(id = "usgasputin")
	public WebElement gasAddedToTank;

	@FindBy(id = "usgasprice")
	public WebElement gasPricePerGallon;

	@FindBy(css = "#standard tr:nth-child(5) input")
	public WebElement calculateButton;

	@FindBy(xpath = "//b[contains(.,'miles per gallon')]")
	public WebElement result;

}
