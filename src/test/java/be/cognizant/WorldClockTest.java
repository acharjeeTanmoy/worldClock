package be.cognizant;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import com.google.common.io.Files;

public class WorldClockTest {

	public WebDriver driver;
	public String baseUrl;
	public WebDriverWait wait;
	public JavascriptExecutor jsExecutor;

	@BeforeClass
	public void DriverSetUp(){
		driver = new EdgeDriver();
		driver.manage().window().maximize();
		baseUrl = "https://cognizantonline.sharepoint.com/sites/Be.Cognizant/SitePages/Home.aspx";
		driver.get(baseUrl);
		jsExecutor = (JavascriptExecutor) driver;
	}
	
	@Test(priority=0) 
	public void userInformation() throws InterruptedException, IOException{

		wait = new WebDriverWait(driver,Duration.ofSeconds(30));
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("O365_MainLink_Help_container")));
		//*[@id="O365_MainLink_Settings"]/div/span
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"O365_MainLink_Settings\"]/div/span")));
		Thread.sleep(1000);
		WebElement userIcon = driver.findElement(By.id("meInitialsButton"));
		Actions actions = new Actions(driver);
		actions.moveToElement(userIcon).build().perform();
		//actions.doubleClick(userIcon).perform();
		jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.id("meInitialsButton")));
		Thread.sleep(1000);

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("mectrl_currentAccount_secondary")));
		String account = driver.findElement(By.id("mectrl_currentAccount_secondary")).getText();
		Assert.assertEquals(account,"2318235@cognizant.com");
		System.out.println("Account details is verified");

		File screenshotFile =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(screenshotFile, new File("C:\\Users\\2318235\\OneDrive - Cognizant\\Pictures\\CASWorld\\1_account.jpg"));

		//wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//*[@id=\"spPageChromeAppDiv\"]/div/div[2]/div[4]/section/article/div/div")));
//		Thread.sleep(3000);
		jsExecutor.executeScript("arguments[0].click();", driver.findElement(By.id("meInitialsButton")));
	}
	
	@Test(priority=1)
	public void clock() throws InterruptedException, IOException {
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='vpc_WebPart.WorldClockWebPart.internal.60655e4a-73c8-49d0-9571-c762791557af']")));
		jsExecutor.executeScript("arguments[0].scrollIntoView();", driver.findElement(By.xpath("//div[@id='vpc_WebPart.WorldClockWebPart.internal.60655e4a-73c8-49d0-9571-c762791557af']")));
		//Thread.sleep(3000);

		File worldClock =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(worldClock, new File("C:\\Users\\2318235\\OneDrive - Cognizant\\Pictures\\CASWorld\\2_worldClock.jpg"));

		Boolean worldClock_display = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='vpc_WebPart.WorldClockWebPart.internal.60655e4a-73c8-49d0-9571-c762791557af']"))).isDisplayed();
		//Boolean Display = driver.findElement(By.xpath("//div[@id='vpc_WebPart.WorldClockWebPart.internal.60655e4a-73c8-49d0-9571-c762791557af']")).isDisplayed();
		System.out.println("The World Clock section is displayed :"+worldClock_display);


		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();

		String datepattern = currentDate.format(DateTimeFormatter.ofPattern("M/dd/yyyy"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma");
		String formattedTime = currentTime.format(formatter);

//		Thread.sleep(1200);
		String date = driver.findElement(By.xpath("(//div[@class='n_b_816e1fa6']/div[2])[1]")).getText().split(" ")[1];
		String time = driver.findElement(By.xpath("(//div[@data-automation-id = 'clock-card']//div[@data-automation-id = 'clock-card-AM-PM-time'])[1]")).getText();

		Assert.assertEquals(datepattern,date);
		Assert.assertEquals(formattedTime,time);
		System.out.println("Current date and Week details are displayed in World clock, is verified with System Date: "+datepattern);
		System.out.println("Bangalore location Time zone is verified with the System Time: "+formattedTime);
		
		String newyorkTime = driver.findElement(By.xpath("(//div[@data-automation-id = 'clock-card']//div[@data-automation-id = 'clock-card-AM-PM-time'])[3]")).getText();
//		Thread.sleep(1200);
		String londonTime = driver.findElement(By.xpath("(//div[@data-automation-id = 'clock-card']//div[@data-automation-id = 'clock-card-AM-PM-time'])[2]")).getText();

		String LondonTimeDiff = driver.findElement(By.xpath("(//div[@data-automation-id='clock-card-time-offset'])[2]")).getText();
		Assert.assertEquals(LondonTimeDiff, "5h 30m behind");
		System.out.println("Time zone difference is verified correctly for London Location.");
		String NewyorkTimeDiff= driver.findElement(By.xpath("(//div[@data-automation-id='clock-card-time-offset'])[3]")).getText();
		Assert.assertEquals(NewyorkTimeDiff, "9h 30m behind");
		System.out.println("Time zone difference is verified correctly for New York Location.");
//		Thread.sleep(1200);
		driver.navigate().to("https://www.google.com/search?q=new+york+time&rlz=1C1GCEU_enIN1094IN1094&oq=new+york+time&gs_lcrp=EgZjaHJvbWUyEQgAEEUYORhDGLEDGIAEGIoFMgwIARAAGEMYgAQYigUyDAgCEAAYQxiABBiKBTIMCAMQABhDGIAEGIoFMgoIBBAuGLEDGIAEMg8IBRAuGEMYsQMYgAQYigUyDAgGEC4YQxiABBiKBTISCAcQABhDGIMBGLEDGIAEGIoFMgwICBAuGEMYgAQYigUyDwgJEAAYQxixAxiABBiKBdIBCTQ3MTRqMGoxNagCALACAA&sourceid=chrome&ie=UTF-8");

		String newYorkTime_unformatted = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='gsrt vk_bk FzvWSb YwPhnf']"))).getText();

		String newYorkTime_google;
		if(newYorkTime_unformatted.length() == 7) {
			String googleTime = newYorkTime_unformatted.substring(0, 4);
			String timePeriod = newYorkTime_unformatted.substring(5).toUpperCase();
			newYorkTime_google = googleTime.concat(timePeriod);
		}
		else {
			String googleTime_NY = newYorkTime_unformatted.substring(0, 5);
			String timePeriod_NY = newYorkTime_unformatted.substring(6).toUpperCase();
			newYorkTime_google = googleTime_NY.concat(timePeriod_NY);
		}

//		Thread.sleep(1200);
		driver.navigate().to("https://www.google.com/search?q=london+time&rlz=1C1GCEU_enIN1094IN1094&oq=london+time&gs_lcrp=EgZjaHJvbWUyBggAEEUYOTINCAEQABiDARixAxiABDINCAIQABiDARixAxiABDINCAMQABiDARixAxiABDIHCAQQABiABDITCAUQLhiDARjHARixAxjRAxiABDINCAYQABiDARixAxiABDINCAcQABiDARixAxiABDIHCAgQABiABDIWCAkQLhiDARjHARixAxjRAxiABBiKBdIBCDI3OTZqMGo3qAIAsAIA&sourceid=chrome&ie=UTF-8");

		String londonTime_unformatted = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='gsrt vk_bk FzvWSb YwPhnf']"))).getText();
		//String googleData1 = driver.findElement(By.xpath("//div[@class='gsrt vk_bk FzvWSb YwPhnf']")).getText();


		String londonTime_google;
		if(londonTime_unformatted.length() == 7) {
			String googleTime_london = londonTime_unformatted.substring(0, 4);
			String timePeriod_london = londonTime_unformatted.substring(5).toUpperCase();
			londonTime_google = googleTime_london.concat(timePeriod_london);
		}
		else {
			String googleTime_london = londonTime_unformatted.substring(0, 5);
			String timePeriod_london = londonTime_unformatted.substring(6).toUpperCase();
			londonTime_google = googleTime_london.concat(timePeriod_london);
		}

		Assert.assertEquals(newYorkTime_google, newyorkTime);	
		Assert.assertEquals(londonTime_google,londonTime);
//		Thread.sleep(1200);

		System.out.println("London Time Zone: " + londonTime + " and New York Time zone: " + newyorkTime +" with the Google data is verified.");
//		Thread.sleep(1200);
		driver.get(baseUrl);
//		Thread.sleep(5000);

	}
	@Test(priority=2)
	public void oneCognizant() throws IOException, InterruptedException {
		//JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
		jsExecutor.executeScript("document.getElementsByClassName('m_b_beed2cf1')[0].scrollBy(0,400)");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//img[@title='OneCognizant']")));
		String BeCognizantHandle = driver.getWindowHandle();
		driver.findElement(By.xpath("//img[@title='OneCognizant']")).click();

		Set<String> wh = driver.getWindowHandles();
		for(String s:wh) {
			if(s.equals(BeCognizantHandle)) {
			}
			else {
				driver.switchTo().window(s);
			}
		}

		jsExecutor.executeScript("window.scrollTo(0,document.body.scrollHeight)");
		WebElement viewAll = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='View All Apps']")));
		viewAll.click();
		//

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='aZHolder']//div")));

		//Thread.sleep(1000);
		File allApps =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(allApps, new File("C:\\Users\\2318235\\OneDrive - Cognizant\\Pictures\\CASWorld\\3_allApps.jpg"));
		//Thread.sleep(1000);

		List<WebElement> alphabets = driver.findElements(By.xpath("//div[@class='aZHolder']//div"));
		
		for(WebElement alphabet : alphabets) {
			if(alphabet.isDisplayed()) {
				System.out.println(alphabet.getText() + " is Displayed.");
			}
			else {
				System.out.println(alphabet.getText() + " is not Displayed");
			}
		}

		driver.findElement(By.xpath("//div[text()='J']")).click();
		String jTab_element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@aria-label='JLL Client Portal Opens in a new tab']/div"))).getText();
		//String text=jTab.getText();
		File jTabSS =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		Files.copy(jTabSS, new File("C:\\Users\\2318235\\OneDrive - Cognizant\\Pictures\\CASWorld\\4_jllClient.jpg"));
		System.out.println("Element present under J option are : " + jTab_element);
		//Thread.sleep(2000);
		// jTabSS =((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		//Thread.sleep(2000);
//		Files.copy(jTabSS, new File("C:\\Users\\2318235\\OneDrive - Cognizant\\Pictures\\CASWorld\\4_jllClient.jpg"));
		//Thread.sleep(1000);	
//
	}
	@AfterClass
	public void tearDown(){
		driver.quit();
	}
}