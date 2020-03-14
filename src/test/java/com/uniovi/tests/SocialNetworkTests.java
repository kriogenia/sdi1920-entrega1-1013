package com.uniovi.tests;

import java.util.List;
import static org.junit.Assert.*;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.uniovi.tests.pageobjects.PO_HomeView;
import com.uniovi.tests.pageobjects.PO_LoginView;
import com.uniovi.tests.pageobjects.PO_PrivateView;
import com.uniovi.tests.pageobjects.PO_Properties;
import com.uniovi.tests.pageobjects.PO_RegisterView;
import com.uniovi.tests.pageobjects.PO_View;
import com.uniovi.tests.util.SeleniumUtils;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class SocialNetworkTests {

	// CARMEN
	static String PathFirefox65 = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe";
	static String Geckdriver024 = "D:\\UNI\\Tercero\\SDI\\Sesion 5\\Material\\geckodriver024win64.exe";

	// RICHI
//	static String PathFirefox65 = "D:\\Mozilla Firefox\\firefox.exe";
//	static String Geckdriver024 = "E:\\Clase\\SDI\\Material\\PL-SDI-Sesión5-material\\geckodriver024win64.exe";

	static WebDriver driver = getDriver(PathFirefox65, Geckdriver024);
	static String URL = "http://localhost:8090";

	public static WebDriver getDriver(String PathFirefox, String Geckdriver) {
		System.setProperty("webdriver.firefox.bin", PathFirefox);
		System.setProperty("webdriver.gecko.driver", Geckdriver);
		WebDriver driver = new FirefoxDriver();
		return driver;
	}

	/**
	 * Before each test we navigate to the homepage
	 */
	@Before
	public void setUp() {
		driver.navigate().to(URL);
	}

	/**
	 * After each test we delete the cookies
	 */
	@After
	public void tearDown() {
		driver.manage().deleteAllCookies();
	}

	@BeforeClass
	static public void begin() {
	}

	/**
	 * After the last test we close the explorer
	 */
	@AfterClass
	static public void end() {
		driver.quit();
	}

	/********************************************************************************
	 * USER LIST TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR11 - Show the list of users and check that it list all the registered users
	 * on the system
	 */
	@Test
	public void PR11() {
		logAs("rachel@friends.com", "123");

		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		// Todos menos el admin y rachel (Chandler, Monica, Phoebe, Joey y Ross)
		assertTrue(elementos.size() == 5);
		PO_View.checkElement(driver, "text", "Phoebe");
		PO_View.checkElement(driver, "text", "Chandler");
		PO_View.checkElement(driver, "text", "Monica");
		PO_View.checkElement(driver, "text", "Joey");
		PO_View.checkElement(driver, "text", "Ross");
	}

	/********************************************************************************
	 * USER SEARCH TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR12 - Empty search must return the whole users list
	 */
	@Test
	public void PR12() {
		logAs("rachel@friends.com", "123");

		SeleniumUtils.esperarSegundos(driver, 1);

		search("");

		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		// Todos menos el admin y rachel (Chandler, Monica, Phoebe, Joey y Ross)
		assertTrue(elementos.size() == 5);
		PO_View.checkElement(driver, "text", "Phoebe");
		PO_View.checkElement(driver, "text", "Chandler");
		PO_View.checkElement(driver, "text", "Monica");
		PO_View.checkElement(driver, "text", "Joey");
		PO_View.checkElement(driver, "text", "Ross");
	}

	/**
	 * PR13 - Search with inexsitant text must return an empty list
	 */
	@Test
	public void PR13() {
		logAs("rachel@friends.com", "123");

		SeleniumUtils.esperarSegundos(driver, 1);

		search("Patata");

		// Como no debería aparecer ningún usuario tampoco debería aparecer ningún email (Todos los emails deben llevar "@")
		SeleniumUtils.EsperaCargaPaginaNoTexto(driver, "@", 1);
	}

	/**
	 * PR14 - Specific search showing the correct list
	 */
	@Test
	public void PR14() {
		logAs("rachel@friends.com", "123");
		
		SeleniumUtils.esperarSegundos(driver, 1);

		search("le");

		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		// Todos menos el admin y rachel (Chandler, Monica, Phoebe, Joey y Ross)
		assertTrue(elementos.size() == 3);
		PO_View.checkElement(driver, "text", "Chandler"); // Por el nombre
		PO_View.checkElement(driver, "text", "Monica"); // Por el apellido
		PO_View.checkElement(driver, "text", "Ross"); // Por el apellido
	}

	/********************************************************************************
	 * NEW POST TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR24 - Go to the form of creating publications, fill it with valid data and
	 * using the submit button. Check the publication appears in the list of
	 * publications of the user.
	 */
	@Test
	public void PR24() {
		logAs("rachel@friends.com", "123");
		
		// Go to Add Publication
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/add')]");
		elementos.get(0).click();
		
		// Fill form
		PO_PrivateView.fillFormAddPublication(driver, "Publicación 1", "Ejemplo de publicación");
		
		// Go to List publications
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/list')]");
		elementos.get(0).click();

		elementos = PO_View.checkElement(driver, "text", "Publicación 1");

		PO_PrivateView.clickOption(driver, "logout", "text", "Login");
	}

	/**
	 * PR25 - Go to the form of creating publications, fill it with invalida data
	 * (empty title) and using submit. Check the warning shows up.
	 */
	@Test
	public void PR25() {
		logAs("rachel@friends.com", "123");
				
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/add')]");
		elementos.get(0).click();
		
		
		PO_PrivateView.fillFormAddPublication(driver, "", "Ejemplo de publicación");
		PO_View.getP();
		PO_RegisterView.checkKey(driver, "Error.publication.title.length", PO_Properties.getSPANISH());
		
		// Ahora nos desconectamos
		PO_PrivateView.clickOption(driver, "logout", "text", "Login");
	}

	/********************************************************************************
	 * LIST MY POSTS TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR26 - Show post list of an user and check all its publcations are listed
	 */
	@Test
	public void PR26() {
		logAs("rachel@friends.com", "123");
		
		// Go to List Publication
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/list')]");
		elementos.get(0).click();
		
		// Check she has 3 publications (2 + 1)
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 3);

		PO_View.checkElement(driver, "text", "Title1");
		PO_View.checkElement(driver, "text", "Title7");
	}

	/********************************************************************************
	 * LIST MY FRIEND'S POST TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR27 - Show the publications of a friend and check it list all.
	 */
	@Test
	public void PR27() {
		logAs("rachel@friends.com", "123");
		
		// Go to List Friends
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'friends-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'friends/list')]");
		elementos.get(0).click();
		
		// Go to Phoebe publicatios
		elementos = PO_View.checkElement(driver, "free",
		"//td[contains(text(), 'phoebe')]/following-sibling::*/a[contains(@class,'btn')]");
		System.out.println(elementos.size());
		elementos.get(0).click();
				
		// Check Phoebe has 1 publication
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		
		PO_View.checkElement(driver, "text", "Title4");
	}

	/**
	 * PR28 - Using an access through URL try to list the publications of and user
	 * we are not friend with. Check that the system shows a error warning.
	 */
	@Test
	public void PR28() {
		logAs("rachel@friends.com", "123");
		
		driver.navigate().to("http://localhost:8090/publication/list/ross@friends.com");
		
		PO_View.checkElement(driver, "text", "Bienvenido a la página");
	}

	/********************************************************************************
	 * NEW POST WITH IMAGE TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR29 - From the form of creating a publication, create a valid publciation
	 * with an image. Check the image shows up with the publication.
	 */
	@Test
	public void PR29() {
		logAs("chandler@friends.com", "123");
		
		// Go to List Friends
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/list')]");
		elementos.get(0).click();
				
		// has an image in one post
		elementos = PO_View.checkElement(driver, "class", "img-thumbnail");
		assertTrue(elementos.size() == 1);
	}

	/**
	 * PR30 - Create a publication with valid data without image. Check the
	 * publication is created and listed correctly without the image.
	 */
	@Test
	public void PR30() {
		logAs("phoebe@friends.com", "123");
		
		// Go to Add Publication
		List<WebElement> elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/add')]");
		elementos.get(0).click();
		
		// Fill form
		PO_PrivateView.fillFormAddPublication(driver, "Publicación 1", "Ejemplo de publicación");
		
		// Go to List publications
		elementos = PO_View.checkElement(driver, "free", "//li[contains(@id,'publications-menu')]/a");
		elementos.get(0).click();
		
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@href, 'publication/list')]");
		elementos.get(0).click();

		elementos = PO_View.checkElement(driver, "text", "Publicación 1");

		// Check there are no images
		try {
			elementos = PO_View.checkElement(driver, "class", "img-thumbnail");
			fail();
		} catch (Exception e) {
			
		}
	}

	/********************************************************************************
	 * ADMIN LISTING USERS TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR31 - Show the list of users and check it does show all the users in the
	 * system.
	 */
	@Test
	public void PR31() {
		logAs("admin@email.com", "admin");

		// Contamos el número de filas de notas
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		// Todos menos el admin y ross que está en la siguiente pagina (Chandler, Monica, Phoebe, Joey y Rachel)
		assertTrue(elementos.size() == 5);
		PO_View.checkElement(driver, "text", "Rachel");
		PO_View.checkElement(driver, "text", "Phoebe");
		PO_View.checkElement(driver, "text", "Chandler");
		PO_View.checkElement(driver, "text", "Monica");
		PO_View.checkElement(driver, "text", "Joey");
		
		// Vamos a la siguiente página
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@class,'page-link')]");
		elementos.get(3).click();

		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
		
		PO_View.checkElement(driver, "text", "Ross");
	}

	/********************************************************************************
	 * ADMIN MULTIPLE DELETE TESTS
	 * 
	 ********************************************************************************/

	/**
	 * PR32 - List the users and delete the first user on the list. Check the list
	 * updates and the user dissappears.
	 */
	@Test
	public void PR32() {
		logAs("admin@email.com", "admin");
		
		// Comprobamos que está Rachel (Siempre aparece la primera)
		PO_View.checkElement(driver, "text", "Rachel");
					
		// Borramos el primero
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), '')]/following-sibling::*/input[contains(@name,'cb')]");
		elementos.get(0).click();
		By boton = By.id("deleteButton");
		driver.findElement(boton).click();
		
		// Comprobamos que no está Rachel pero están los otros 5
		SeleniumUtils.textoNoPresentePagina(driver, "Rachel");
		
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
				PO_View.getTimeout());
		assertTrue(elementos.size() == 5);
		
		// La segunda página debería estar vacía
		elementos = PO_View.checkElement(driver, "free", "//a[contains(@class,'page-link')]");
		elementos.get(3).click();

		try {
			elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
			PO_View.getTimeout());
			fail();
		} catch (Exception e) {
					
		}
	}

	/**
	 * PR33 - List the users and delete the last user on the list. Check the list
	 * updates and the user dissappears.
	 */
	@Test
	public void PR33() {
		logAs("admin@email.com", "admin");
		
		// Comprobamos que está Ross (Siempre aparece el último)
		PO_View.checkElement(driver, "text", "Ross");
							
		// Borramos el último (ahora solo hay una página)
		List<WebElement> elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), '')]/following-sibling::*/input[contains(@name,'cb')]");
		elementos.get(elementos.size()-1).click();
		By boton = By.id("deleteButton");
		driver.findElement(boton).click();
				
		// Comprobamos que solo quedan 4 
		SeleniumUtils.textoNoPresentePagina(driver, "Ross");
			
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
					PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
	}

	/**
	 * PR34 - List the users, delete three users and check the list updates and
	 * those users disappears.
	 */
	@Test
	public void PR34() {
		logAs("admin@email.com", "admin");
		
		// Comprobamos que quedan 4 					
		List<WebElement> elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
							PO_View.getTimeout());
		assertTrue(elementos.size() == 4);
							
		// Borramos el último (ahora solo hay una página)
		elementos = PO_View.checkElement(driver, "free",
				"//td[contains(text(), '')]/following-sibling::*/input[contains(@name,'cb')]");
		elementos.get(0).click();
		elementos.get(1).click();
		elementos.get(2).click();
		By boton = By.id("deleteButton");
		driver.findElement(boton).click();
				
		// Comprobamos que solo quedan 4
			
		elementos = SeleniumUtils.EsperaCargaPagina(driver, "free", "//tbody/tr",
					PO_View.getTimeout());
		assertTrue(elementos.size() == 1);
	}

	/********************************************************************************
	 * HELPING AND NAVIGATION METHODS
	 * 
	 ********************************************************************************/
	private void search(String search) {
		WebElement email = driver.findElement(By.name("searchText"));
		email.click();
		email.clear();
		email.sendKeys(search);
		By boton = By.id("searchBtn");
		driver.findElement(boton).click();
	}

	private void logAs(String email, String password) {
		// Vamos al formulario de logueo.
		PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
		// Rellenamos el formulario
		PO_LoginView.fillForm(driver, email, password);
		// Comprobamos que entramos en la pagina privada de Alumno
		PO_View.checkElement(driver, "id", "userHeader");
	}

}