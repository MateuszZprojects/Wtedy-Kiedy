package controllers;


import common.TAccount;
import common.TCard;
import common.TGame;
import common.TGameCard;
import controllers.models.Account;
import controllers.models.Card;
import controllers.models.Game;
import controllers.models.GameCard;
import controllers.repositories.AccountRepository;
import controllers.repositories.CardRepository;
import controllers.repositories.GameCardRepository;
import controllers.repositories.GameRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MyController {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    GameRepository gameRepository;

    @Autowired
    GameCardRepository gamecardRepository;



    @Operation(summary = "Check system status", description = "Returns the status of the system along with some additional information.")
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String systemStatus() {
        List<Object[]> results = gamecardRepository.findYear();
        for (Object[] result : results) {
            System.out.println("gameId: " + result[0] + ", playerId: " + result[1] + ", cardId: " + result[2] + ", cardYear: " + result[3]);
        }
        return "Ok";    }

    @Operation(summary = "Login", description = "Returns the permission to login on an account if system found in database and it isn't marked as deleted.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleLogin(@RequestBody Map<String, String> loginRequest) {
        try {
            String login = loginRequest.get("login");
            String password = loginRequest.get("password");
            Account account = accountRepository.findByLoginAndPasswordAndDeletedFalse(login, password);
            if (account == null) {                throw new IllegalArgumentException("Nieprawidłowy login lub hasło");            }
            String userLogin = account.getLogin();
            String userId = Integer.toString(account.getId());
            String userRole = account.getType();
            Map<String, String> response = new HashMap<>();
            response.put("userLogin", userLogin);
            response.put("userId", userId);
            response.put("userRole", userRole);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Handle authentication error
            return new ResponseEntity<>(Collections.singletonMap("error", "Invalid login or password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Register system", description = "Creates new account and returns answer to let new user to login or not.")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> addAccount(@RequestBody String jsonString) {
        try {
            System.out.println("Nowe konto!");
            JSONObject obj = new JSONObject(jsonString);
            if (obj.has("login") && obj.has("password")) {
                String login = obj.getString("login");
                String password = obj.getString("password");
                if (accountRepository.existsByLogin(login)) { return new ResponseEntity<>("Login already exists", HttpStatus.BAD_REQUEST);  }
                Account newAccount = new Account();
                newAccount.setLogin(login);
                newAccount.setPassword(password);
                accountRepository.save(newAccount);
                return new ResponseEntity<>("New account added successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid request data", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error adding new account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Assign card to the player during game", description = "Receives payload with informations to check if player decision is correct, then assigns card.")
    @RequestMapping(value = "/addselect", method = RequestMethod.POST)
    public ResponseEntity<String> addSelect(@RequestBody Map<String, Object> payload) {
        try {
            String time = (String) payload.get("time");
            int cardYear = Integer.parseInt(payload.get("cardYear").toString());
            int pickedYear = Integer.parseInt(payload.get("pickedYear").toString());
            int userID = Integer.parseInt(payload.get("userID").toString());
            int gameID = Integer.parseInt(payload.get("gameID").toString());
            String gameRole = payload.get("gameRole").toString();

            Game game = gameRepository.findById(gameID);

            Account user1 = accountRepository.findByLogin(game.getPlayer1login());
            Account user2 = accountRepository.findByLogin(game.getPlayer2login());

            System.out.println("User1: "+user1.getId());
            System.out.println("User2: "+user2.getId());
            GameCard cardMid = gamecardRepository.findByGameidAndPlayerid(gameID, 0); // Assuming you have a method findByGameId in your repository

            if (time.equals("after")) {
                System.out.print("After: ");
                if (cardYear > pickedYear) {
                    System.out.println("Good Time");
                    Long cardsExist = gamecardRepository.countGameCardsBetweenYears(gameID, userID, pickedYear, cardYear) ;
                    if(cardsExist>1){
                        System.out.println("Cards exist between " + pickedYear + " and " + cardYear);
                        if("player2".equals(gameRole)) {
                            cardMid.setPlayerid(user1.getId());
                            game.setPlayer1score(game.getPlayer1score()+1);
                        }
                        else if("player1".equals(gameRole)) {
                            cardMid.setPlayerid(user2.getId());
                            game.setPlayer2score(game.getPlayer2score()+1);
                        }
                    }
                    else {
                        System.out.println("No cards exist between " + pickedYear + " and " + cardYear);
                        if("player2".equals(gameRole)) {
                            cardMid.setPlayerid(user2.getId());
                            game.setPlayer2score(game.getPlayer2score()+1);
                        }
                        else if("player1".equals(gameRole)) {
                            cardMid.setPlayerid(user1.getId());
                            game.setPlayer1score(game.getPlayer1score()+1);
                        }
                    }
                } else {
                    System.out.println("Bad");
                    if("player2".equals(gameRole)) {
                        cardMid.setPlayerid(user1.getId());
                        game.setPlayer1score(game.getPlayer1score()+1);
                    }
                    else if("player1".equals(gameRole)) {
                        cardMid.setPlayerid(user2.getId());
                        game.setPlayer2score(game.getPlayer2score()+1);
                    }
                }
            } else {
                System.out.print("Before: ");
                if (pickedYear > cardYear) {
                    System.out.println("Good Time");
                    Long cardsExist = gamecardRepository.countGameCardsBetweenYears(gameID, userID, cardYear, pickedYear) ;
                    if(cardsExist>1){
                        System.out.println("Cards exist between " + cardYear + " and " + pickedYear);
                        if("player2".equals(gameRole)) {
                            cardMid.setPlayerid(user1.getId());
                            game.setPlayer1score(game.getPlayer1score()+1);
                        }
                        else if("player1".equals(gameRole)) {
                            cardMid.setPlayerid(user2.getId());
                            game.setPlayer2score(game.getPlayer2score()+1);
                        }
                    } else {
                        System.out.println("No cards exist between " + cardYear + " and " + pickedYear);
                        if("player2".equals(gameRole)) {
                            cardMid.setPlayerid(user2.getId());
                            game.setPlayer2score(game.getPlayer2score()+1);
                        }
                        else if("player1".equals(gameRole)) {
                            cardMid.setPlayerid(user1.getId());
                            game.setPlayer1score(game.getPlayer1score()+1);
                        }
                    }
                } else {
                    System.out.println("Bad");
                    if("player2".equals(gameRole)) {
                        cardMid.setPlayerid(user1.getId());
                        game.setPlayer1score(game.getPlayer1score()+1);
                    }
                    else if("player1".equals(gameRole)) {
                        cardMid.setPlayerid(user2.getId());
                        game.setPlayer2score(game.getPlayer2score()+1);
                    }
                }
            }
            gamecardRepository.save(cardMid);
            System.out.println(cardMid);
            DrawACard(gameID, 0);

            return new ResponseEntity<>("Entity updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating entity: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Get game score", description = "Returns informations about game based on received game ID.")
    @RequestMapping(value = "/getscore", method = RequestMethod.GET)
    public ResponseEntity<TGame> getScore(@RequestParam int gameId) {
        try {
            Game game = gameRepository.findById(gameId);

            if (game == null) {
                throw new IllegalArgumentException("Nie ma danych");
            }

            TGame locGame = new TGame();
            locGame.setPlayer1login(game.getPlayer1login());
            locGame.setPlayer1score(game.getPlayer1score());
            locGame.setPlayer2login(game.getPlayer2login());
            locGame.setPlayer2score(game.getPlayer2score());
            locGame.setDeleted(game.isDeleted());

            ResponseEntity<TGame> res = new ResponseEntity<>(locGame, HttpStatus.OK);
            return res;
        } catch (Exception e) {
            TGame locGame = new TGame();
            locGame.setPlayer1login("ERROR: " + e.getMessage());

            ResponseEntity<TGame> res = new ResponseEntity<>(locGame, HttpStatus.OK);
            return res;
        }
    }


    @Operation(summary = "Show cards in database", description = "Returns all cards in database.")
    @RequestMapping(value = "/getcards", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<TCard>> getCards(ServletRequest request) {
        try        {
            List<Card> cardList = cardRepository.findAll();
            if (cardList==null)            {                           throw new IllegalArgumentException("Nie ma danych");}
            ArrayList<TCard> locCardList = new ArrayList<TCard>();
            for (int i=0; i<cardList.size(); i++)            {
                Card card    = cardList.get(i);
                TCard locCard = new TCard(card);
                locCardList.add(locCard);
            }
            ResponseEntity<ArrayList<TCard>> res = new ResponseEntity(locCardList, HttpStatus.OK);
            return res;
        }        
        catch (Exception e)        {
            ArrayList<TCard> locTransferList = new ArrayList<TCard>();
            TCard locTransfer = new TCard();
            locTransfer.setDescription("ERROR:"+e.getMessage());
            locTransferList.add(locTransfer);
            ResponseEntity<ArrayList<TCard>> res = new ResponseEntity(locTransferList, HttpStatus.OK);
            return res;
        }
    }

    @Operation(summary = "Show all cards in specific game", description = "Returns list of cards assigned to specific game.")
    @RequestMapping(value = "/getgamecards", method = RequestMethod.GET)
    public List<TGameCard> getGamecards(@RequestParam Integer gameId) {
        if(gameId==null) gameId=1;
        List<GameCard> gameCards = gamecardRepository.findByGameid(gameId); // Assuming you have a method findByGameId in your repository

        List<TGameCard> gameCardDTOs = new ArrayList<>();

        for (GameCard gameCard : gameCards) {
            // Stworzenie DTO z informacjami o karcie
            TGameCard gameCardDTO = new TGameCard();
            gameCardDTO.setId(gameCard.getId());
            gameCardDTO.setGameid(gameCard.getGameid());
            gameCardDTO.setCard(gameCard.getCardid());
            gameCardDTO.setPlayerid(gameCard.getPlayerid());

            // Dodanie informacji o karcie (rok i opis)
            Card card = gameCard.getCardid(); // Załóżmy, że masz pole karta w encji GameCard
            gameCardDTO.setYear(card.getYear());
            gameCardDTO.setDescription(card.getDescription());
            gameCardDTO.setCardId(card.getId());

            gameCardDTOs.add(gameCardDTO);
        }

        return gameCardDTOs;
    }



    @Operation(summary = "Show all accounts", description = "Return all accounts in database.")
    @RequestMapping(value = "/getaccounts", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<TAccount>> getAccounts(ServletRequest request) {
        try
        {
            List<Account> accountsList = accountRepository.findAll();

            if (accountsList==null)            {                throw new IllegalArgumentException("Nie ma danych");            }
            ArrayList<TAccount> locAccountList = new ArrayList<TAccount>();
            for (int i=0; i<accountsList.size(); i++)            {
                Account account    = accountsList.get(i);
                TAccount locAccount = new TAccount(account);
                locAccountList.add(locAccount);
            }
            ResponseEntity<ArrayList<TAccount>> res = new ResponseEntity(locAccountList, HttpStatus.OK);
            return res;
        }
        catch (Exception e)        {
            ArrayList<TAccount> locAccountList = new ArrayList<TAccount>();
            TAccount locAccount = new TAccount();
            locAccount.setLogin("ERROR:"+e.getMessage());
            locAccountList.add(locAccount);
            ResponseEntity<ArrayList<TAccount>> res = new ResponseEntity(locAccountList, HttpStatus.OK);
            return res;
        }
    }

    @Operation(summary = "Show all games", description = "Return all games in database.")
    @RequestMapping(value = "/getgames", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<TGame>> getGames(ServletRequest request) {
        try        {
            List<Game> gamesList = gameRepository.findAll();
            if (gamesList==null)            {                throw new IllegalArgumentException("Nie ma danych");            }
            ArrayList<TGame> locGameList = new ArrayList<TGame>();
            for (int i=0; i<gamesList.size(); i++)            {
                Game game    = gamesList.get(i);
                TGame locGame = new TGame(game);
                locGameList.add(locGame);
            }
            ResponseEntity<ArrayList<TGame>> res = new ResponseEntity(locGameList, HttpStatus.OK);
            return res;
        }
        catch (Exception e)        {
            ArrayList<TGame> locGameList = new ArrayList<TGame>();
            TGame locGame = new TGame();
            locGame.setPlayer1login("ERROR:"+e.getMessage());
            locGameList.add(locGame);
            ResponseEntity<ArrayList<TGame>> res = new ResponseEntity(locGameList, HttpStatus.OK);
            return res;
        }
    }


    @Operation(summary = "Set card as deleted", description = "Updates card to show as deleted.")
    @RequestMapping(value = "/deletecard", method = RequestMethod.POST)
    public ResponseEntity<String> deleteCard(@RequestBody String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            String cardidtodelete = obj.getString("cardidtodelete");
            int intCardidtodelete = Integer.parseInt(cardidtodelete);
            Card locCard = cardRepository.findById(intCardidtodelete);
            System.out.println("Usunieto karte: "+intCardidtodelete);
            if (locCard == null) { throw new IllegalArgumentException("Nie ma w bazie karty o id: " + cardidtodelete); }
            locCard.setDeleted(true);
            cardRepository.save(locCard);
            ResponseEntity<String> res = new ResponseEntity("Oznaczono kartę o id: " + cardidtodelete + " jako usuniętą", HttpStatus.OK);
            return res;
        } catch (Exception e) {
            String text = new String(e.getMessage());
            ResponseEntity<String> res = new ResponseEntity(text, HttpStatus.OK);
            return res;
        }
    }

    @Operation(summary = "Set account as deleted", description = "Updates account to show as deleted.")
    @RequestMapping(value = "/deleteaccount", method = RequestMethod.POST)
    public ResponseEntity<String> deleteAccount(@RequestBody String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            String accountidtodelete = obj.getString("accountidtodelete");
            int intAccountidtodelete = Integer.parseInt(accountidtodelete);
            Account locAccount = accountRepository.findById(intAccountidtodelete);
            System.out.println("Usunieto konto: "+intAccountidtodelete);
            if (locAccount == null) { throw new IllegalArgumentException("Nie ma w bazie konta o id: " + accountidtodelete); }
            locAccount.setDeleted(true);
            accountRepository.save(locAccount);
            ResponseEntity<String> res = new ResponseEntity("Oznaczono konto o id: " + accountidtodelete + " jako usuniętą", HttpStatus.OK);
            return res;
        } catch (Exception e) {
            String text = new String(e.getMessage());
            ResponseEntity<String> res = new ResponseEntity(text, HttpStatus.OK);
            return res;
        }    }


    @Operation(summary = "Set game as deleted", description = "Updates game to show as deleted and sets player2login as 'remored'")
    @RequestMapping(value = "/deletegame", method = RequestMethod.POST)
    public ResponseEntity<String> deleteGame(@RequestBody String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            String gametodelete = obj.getString("gameID");
            int intGameidtodelete = Integer.parseInt(gametodelete);
            Game locGame = gameRepository.findById(intGameidtodelete);
            System.out.println("Usunieto gre: "+intGameidtodelete);
            if (locGame == null) { throw new IllegalArgumentException("Nie ma w bazie gry o id: " + gametodelete); }
            locGame.setPlayer2login("Removed");
            locGame.setDeleted(true);
            gameRepository.save(locGame);
            ResponseEntity<String> res = new ResponseEntity("Oznaczono gre o id: " + gametodelete + " jako usuniętą", HttpStatus.OK);
            return res;
        } catch (Exception e) {
            String text = new String(e.getMessage());
            ResponseEntity<String> res = new ResponseEntity(text, HttpStatus.OK);
            return res;
        }    }

    @Operation(summary = "Change account permissions", description = "Updates account to moderator or player")
    @RequestMapping(value = "/updateaccount", method = RequestMethod.POST)
    public ResponseEntity<String> updateAccount(@RequestBody String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            String accountidtoupdate = obj.getString("accountidtoupdate");
            int intAccountidtoupdate = Integer.parseInt(accountidtoupdate);
            Account locAccount = accountRepository.findById(intAccountidtoupdate);
            System.out.println("Zmieniono konto: " + intAccountidtoupdate);

            if (locAccount == null) {                throw new IllegalArgumentException("Nie ma w bazie konta o id: " + accountidtoupdate);            }
            if ("1".equals(locAccount.getType())) { locAccount.setType("2"); } else { locAccount.setType("1"); }
            accountRepository.save(locAccount);
            //ResponseEntity<String> res = new ResponseEntity("Zmieniono konto o id: " + accountidtoupdate, HttpStatus.OK);
            ResponseEntity<String> res = new ResponseEntity("ok", HttpStatus.OK);
            return res;
        } catch (Exception e) {
            String text = new String(e.getMessage());
            ResponseEntity<String> res = new ResponseEntity(text, HttpStatus.OK);
            return res;
        }
    }

    @Operation(summary = "Add new card to database", description = "Adds new card to database")
    @RequestMapping(value = "/addcard", method = RequestMethod.POST)
    public ResponseEntity<String> addCard(@RequestBody String jsonString) {
        try {
            System.out.println("Nowa karta!");
            JSONObject obj = new JSONObject(jsonString);
            int year = obj.getInt("year");
            String description = obj.getString("description");
            Card newCard = new Card();
            newCard.setYear(year);
            newCard.setDescription(description);
            cardRepository.save(newCard);
            return new ResponseEntity<>("New card added successfully", HttpStatus.OK);
        } catch (Exception e) {            return new ResponseEntity<>("Error adding new card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);        }
    }

    @Operation(summary = "Add new game to database", description = "Adds new game to database")
    @RequestMapping(value = "/addgame", method = RequestMethod.POST)
    public ResponseEntity<Map<String, String>> addGame(@RequestBody String jsonString) {
        try {
            System.out.println("Nowa gra!");
            JSONObject obj = new JSONObject(jsonString);
            String player1Login = obj.getString("login");
            Game newGame = new Game();
            newGame.setPlayer1login(player1Login);
            Game savedGame = gameRepository.save(newGame);
            Map<String, String> response = new HashMap<>();
            response.put("gameId", String.valueOf(savedGame.getId()));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "Error creating new game: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Assign player to existing game", description = "Sets player2login in existing game to let game start by drawing cards")
    @RequestMapping(value = "/joingame", method = RequestMethod.POST)
    public ResponseEntity<String> joinGame(@RequestBody String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            int gameId = obj.getInt("gameId");
            String player2Login = obj.getString("userLogin");
            Game locGame = gameRepository.findById(gameId);
            if (locGame == null) {   throw new IllegalArgumentException("Nie ma w bazie konta o id: " + gameId);            }
            locGame.setPlayer2login(player2Login);
            String player1Login = locGame.getPlayer1login();
            gameRepository.save(locGame);
            System.out.println("Dolaczono do gry");
            DrawACard(gameId,0);

            Account account1 = accountRepository.findByLogin(player1Login);
            if (account1 == null) {
                throw new IllegalArgumentException("Player not found with login: ");
            }
            DrawACard(gameId,account1.getId());
            Account account2 = accountRepository.findByLogin(player2Login);
            if (account2 == null) {
                throw new IllegalArgumentException("Player not found with login: " + player2Login);
            }
            DrawACard(gameId,account2.getId());
            System.out.println("Gracz1: "+player1Login+" Gracz2: "+player2Login);
            return new ResponseEntity<>("Joined game successfully", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating game: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Surrender the game", description = "Ends game earlier and set score of winner at 10")
    @RequestMapping(value = "/surrender", method = RequestMethod.POST)
    public ResponseEntity<String> surrender(@RequestBody String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);
            int gameId = obj.getInt("gameID");
            String playerLogin = obj.getString("userLogin");
            Game locGame = gameRepository.findById(gameId);
            if (locGame == null) {   throw new IllegalArgumentException("Nie ma w bazie gry o id: " + gameId);            }
            System.out.println("Gracz poddal gre");

            Account user1 = accountRepository.findByLogin(locGame.getPlayer1login());
            Account user2 = accountRepository.findByLogin(locGame.getPlayer2login());

            System.out.println("User1: "+user1.getId());
            System.out.println("User2: "+user2.getId());

            Account account = accountRepository.findByLogin(playerLogin);
            if (account == null) {
                throw new IllegalArgumentException("Player not found with login: " + playerLogin);
            }
            if(playerLogin.equals(user1.getLogin())) locGame.setPlayer2score(10);
            else if (playerLogin.equals(user2.getLogin())) locGame.setPlayer1score(10);
            gameRepository.save(locGame);

            return new ResponseEntity<>("Surrendered game successfully", HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error updating game: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

public void DrawACard(int gameId, int playerId){
    List<Card> cardList = cardRepository.findAll();
    Random random = new Random();

    Card newPickedCard;
    do {
        int randomIndex = random.nextInt(cardList.size());
        newPickedCard = cardList.get(randomIndex);
    } while (gamecardRepository.existsByGameidAndCardid(gameId, newPickedCard));
    System.out.println("Gra: "+gameId+" PlayerId: "+playerId+" Karta: "+newPickedCard);
    GameCard gc0 = new GameCard(gameId, playerId, newPickedCard);
    gamecardRepository.save(gc0);

}





}
