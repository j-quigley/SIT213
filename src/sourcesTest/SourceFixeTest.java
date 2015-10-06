package sourcesTest;

import static org.junit.Assert.*;
import information.Information;
import information.InformationNonConforme;

import org.junit.Before;
import org.junit.Test;

import destinations.Destination;
import destinations.DestinationFinale;

import sources.SourceAleatoire;
import sources.SourceFixe;

public class SourceFixeTest {

	private String msg;
	private SourceFixe s;
	private Information inf;
	private Destination d;
	
	@Before
	public void setUp(){
		msg = "0101010";
		s = new SourceFixe(msg);
		d = new DestinationFinale();
		inf = new Information();
		inf.add(false);
		inf.add(true);
		inf.add(false);
		inf.add(true);
		inf.add(false);
		inf.add(true);
		inf.add(false);
		s.connecter(d);
		try {
			s.emettre();
		} catch (InformationNonConforme e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetInformationGeneree() {
		assertEquals("L'information n'a pas été bien générée", inf, s.getInformationGeneree());
	}
	@Test
	public void testGetInformationEmise() {
		assertEquals("L'information émise n'est pas conforme", inf, s.getInformationEmise());
	}

	@Test
	public void testEmettre() {
		assertEquals("L'information reçue par la destination ne correspond pas", inf, d.getInformationRecue());
	}

}
