package sourcesTest;

import static org.junit.Assert.*;

import information.Information;
import information.InformationNonConforme;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import sources.SourceAleatoire;

public class SourceAleatoireTest {

	@Test
	public void testGetInformationEmise() {
		SourceAleatoire s = new SourceAleatoire(5);
		try {
			s.emettre();
		} catch (InformationNonConforme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Information inf = s.getInformationEmise();
		assertEquals("Les informations devraient être identiques", inf, s.getInformationGeneree());
	}

}
