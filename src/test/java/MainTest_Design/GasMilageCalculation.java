package MainTest_Design;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import Pages.mainPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class GasMilageCalculation {

	WebDriver driver;
	String path = "C:\\Users\\NWCFOOD\\Desktop\\testDataforCalGasMilage.xlsx";
	Xls_Reader xlData = new Xls_Reader(path);

	@BeforeTest
	public void setUp() {

		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("https://www.calculator.net/gas-mileage-calculator.html");
		driver.manage().window().maximize();
	}

	@AfterTest
	public void closeUp() {
		driver.quit();
	}

	// here we are getting locators from Page Object
	@Test
	public void calculateGasMilageUsingPOM() {

		int rowCountOfExcel = xlData.getRowCount("data");
		for (int i = 2; i <= rowCountOfExcel; i++) {

			String run = xlData.getCellData("data", "Execute", i);
			if (!run.equalsIgnoreCase("Y")) {
				xlData.setCellData("data", "Status", i, "Skip Requested");
				continue;
			}

			String currentOdomReader = xlData.getCellData("data", "CurrentOR", i);
			String previousOdomReader = xlData.getCellData("data", "PreviousOR", i);
			String gas = xlData.getCellData("data", "Gas", i);
			mainPage main = new mainPage(driver);
			main.currentOdometrReading.clear();
			main.currentOdometrReading.sendKeys(currentOdomReader);
			main.previousOdometrReading.clear();
			main.previousOdometrReading.sendKeys(previousOdomReader);
			main.gasAddedToTank.clear();
			main.gasAddedToTank.sendKeys(gas);
			main.calculateButton.click();
			String gasMilage = main.result.getText();
			gasMilage = gasMilage.replaceAll(" miles per gallon", "");
			System.out.println(gasMilage);
			double co = Double.parseDouble(currentOdomReader);
			double prvo = Double.parseDouble(previousOdomReader);
			double gs = Double.parseDouble(gas);
			double expectedResult = (co - prvo) / gs;

			DecimalFormat df = new DecimalFormat("0.00");
			String expectedResultAfterDecimal = String.valueOf(df.format(expectedResult));
			xlData.setCellData("data", "Expected", i, expectedResultAfterDecimal);

			if (gasMilage.equals(expectedResultAfterDecimal)) {
				xlData.setCellData("data", "Status", i, "PASS");
			} else {
				xlData.setCellData("data", "Status", i, "FAIL");
			}

			xlData.setCellData("data", "Time", i, LocalDateTime.now().toString());
		}

	}

}
