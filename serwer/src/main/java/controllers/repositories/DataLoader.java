package controllers.repositories;

//Dodanie danych na samym poczatku

import controllers.models.Account;
import controllers.models.Card;
import controllers.models.Game;
import controllers.models.GameCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements ApplicationRunner {

   
    
    private CardRepository cardRepository;
    private AccountRepository accountRepository;
    private GameRepository gameRepository;

    private GameCardRepository gamecardRepository;
       
           
    @Autowired
    public DataLoader(CardRepository cardRepository, AccountRepository accountRepository, GameRepository gameRepository, GameCardRepository gamecardRepository)
    {        
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.gameRepository = gameRepository;
        this.gamecardRepository = gamecardRepository;
    }
    
    
    public void run(ApplicationArguments args) 
    {   
        try
        {            

            
            //Karty
            Card card1 = new Card(476, "Upadek Cesarstwa Rzymskiego Zachodniego.");
            Card card2 = new Card(1215, "Ustanowienie Wielkiej Karty Swobód (Magna Carta) w Anglii.");
            Card card3 = new Card(1492, "Odkrycie Ameryki przez Krzysztofa Kolumba.");
            Card card4 = new Card(1789, "Wybuch Rewolucji Francuskiej.");
            Card card5 = new Card(1861, "Rozpoczęcie wojny domowej w Stanach Zjednoczonych.");
            Card card6 = new Card(1914, "Wybuch I wojny światowej.");
            Card card7 = new Card(1917, "Rewolucja Październikowa w Rosji.");
            Card card8 = new Card(1929, "Krach giełdowy na Wall Street, początek Wielkiego Kryzysu.");
            Card card9 = new Card(1939, "Rozpoczęcie II wojny światowej.");
            Card card10 = new Card(1945, "Zakończenie II wojny światowej.");
            Card card11 = new Card(1963, "Zamach na prezydenta USA Johna F. Kennedy'ego.");
            Card card12 = new Card(1969, "Pierwsze lądowanie człowieka na Księżycu (misja Apollo 11).");
            Card card13 = new Card(1989, "Upadek muru berlińskiego.");
            Card card14 = new Card(1991, "Rozpad Związku Radzieckiego.");
            Card card15 = new Card(2001, "Zamachy terrorystyczne 11 września w USA.");
            Card card16 = new Card(2008, "Globalny kryzys finansowy.");
            Card card17 = new Card(2011, "Śmierć lidera terrorystycznego ugrupowania Al-Kaida, Osamy bin Ladena.");
            Card card18 = new Card(2016, "Wybór Donalda Trumpa na prezydenta Stanów Zjednoczonych.");
            Card card19 = new Card(2019, "Pandemia COVID-19.");
            Card card20 = new Card(1968, "Ruchy społeczne i protesty na całym świecie, w tym Praskie Wiosna.");
            Card card21 = new Card(1920, "Ustanowienie 19. poprawki do Konstytucji USA, gwarantującej prawa wyborcze kobiet.");
            Card card22 = new Card(1980, "Powstanie niepodległego związku zawodowego Solidarność w Polsce.");
            Card card23 = new Card(1947, "Plan Marshalla dla odbudowy Europy po II wojnie światowej.");
            Card card24 = new Card(1979, "Rewolucja islamska w Iranie.");
            Card card25 = new Card(1948, "Proklamowanie niepodległości Izraela.");
            Card card26 = new Card(1954, "Rozstrzygnięcie sprawy Brown przeciwko Komisji Szkolnej, zniesienie segregacji rasowej w szkołach w USA.");
            Card card27 = new Card(1986, "Katastrofa w elektrowni jądrowej w Czarnobylu.");
            Card card28 = new Card(2005, "Wprowadzenie małżeństw jednopłciowych w Hiszpanii.");
            Card card29 = new Card(1971, "Ustanowienie systemu jedno dziecko w Chinach.");
            Card card30 = new Card(1994, "Koniec apartheidu w RPA, pierwsze wieloetniczne wybory.");

            // Zapisz karty do repozytorium
            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);
            cardRepository.save(card4);
            cardRepository.save(card5);
            cardRepository.save(card6);
            cardRepository.save(card7);
            cardRepository.save(card8);
            cardRepository.save(card9);
            cardRepository.save(card10);
            cardRepository.save(card11);
            cardRepository.save(card12);
            cardRepository.save(card13);
            cardRepository.save(card14);
            cardRepository.save(card15);
            cardRepository.save(card16);
            cardRepository.save(card17);
            cardRepository.save(card18);
            cardRepository.save(card19);
            cardRepository.save(card20);
            cardRepository.save(card21);
            cardRepository.save(card22);
            cardRepository.save(card23);
            cardRepository.save(card24);
            cardRepository.save(card25);
            cardRepository.save(card26);
            cardRepository.save(card27);
            cardRepository.save(card28);
            cardRepository.save(card29);
            cardRepository.save(card30);

            Account acc1 = new Account("JNowak","nowak","1");
            Account acc2 = new Account("AKowal","kowal","2");
            Account acc3 = new Account("Admin","admin","3");

            accountRepository.save(acc1);
            accountRepository.save(acc2);
            accountRepository.save(acc3);

            Game game0 = new Game("System",10,"System",10, false);
            Game game1 = new Game("JNowak",10,"AKowal",3, false);
            Game game2 = new Game("AKowal",7,"Admin",10, false);

            gameRepository.save(game0);
            gameRepository.save(game1);
            gameRepository.save(game2);

            GameCard gc = new GameCard(1,1,card1);
            gamecardRepository.save(gc);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
   
}



