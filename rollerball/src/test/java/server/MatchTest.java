package server;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import server.matches.Match;
import server.api.MoveRequest;
import server.api.MoveResponse;
import server.api.AuthenticationToken;

public class MatchTest {
	@Test
	public void testMoves() {
		int whiteId = 255;
		int blackId = 25*71;
		Match match = Match.createNewMatch(15, whiteId, blackId);
		MoveRequest request = new MoveRequest();
		MoveResponse response = new MoveResponse();
		AuthenticationToken whiteToken = AuthenticationToken.createFakeToken("Tester1", 1597563, whiteId);
		AuthenticationToken blackToken = AuthenticationToken.createFakeToken("Tester2", 1597986, blackId);
		request.setFakeToken(whiteToken);
		
		request.from = "garbage";
		request.to = "a4";

		response = match.move(request);
		Assert.assertTrue(response.success == false); //testing garbage

		request.from = "c2";
		request.to = "garbage";
		
		response = match.move(request);
		Assert.assertTrue(response.success == false);

		request.setFakeToken(blackToken);
		
		request.from = "c2";
		request.to = "b3";

		response = match.move(request);
		Assert.assertTrue(response.success == false); //black cant move for white
		
		request.setFakeToken(whiteToken);

		request.from = "c2";
		request.to = "b3";
		
		response = match.move(request);
		//System.out.println(response.message);
		Assert.assertTrue(response.success == true); //legal move for white
		//System.out.println(match.getBoard());

		request.from = "b3";
		request.to = "b4";

		response = match.move(request);
		//System.out.println(response.success+" "+response.message);
		Assert.assertTrue("white is moving on not his turn", response.success == false); //white cant move on black's turn

		request.setFakeToken(blackToken);

		request.from = "e6";
		request.to = "f5";
		
		response = match.move(request);
		Assert.assertTrue(response.success == true); //legal move for black

		request.setFakeToken(whiteToken);
	
		request.from = "b3";
		request.to = "b4";

		response = match.move(request);
		Assert.assertTrue(response.success == true); //the following moves are to allow for promotion to be tested

		request.setFakeToken(blackToken);

		request.from = "f5";
		request.to = "f4";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(whiteToken);

		request.from = "d1";
		request.to = "c6";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(blackToken);

		request.from = "f4";
		request.to = "f3";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(whiteToken);

		request.from = "c6";
		request.to = "b5";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(blackToken);

		request.from = "f3";
		request.to = "e2";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(whiteToken);

		request.from = "d2";
		request.to = "c2";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(blackToken);

		request.from = "e2";
		request.to = "d2";
		request.promoteTo = "b";

		response = match.move(request);
		Assert.assertTrue(response.success == true); //this is a fake promotion shouldn't actually promote anything

		request.setFakeToken(whiteToken);

		request.from = "c2";
		request.to = "b2";

		response = match.move(request);
		Assert.assertTrue(response.success == true);

		request.setFakeToken(blackToken);

		request.from = "d2";
		request.to = "c1";
		request.promoteTo = "R";

		response = match.move(request);
		Assert.assertTrue(response.success == true); //do promotion, also works for "r" or "R" for rook and anything else for bishop
		

		System.out.println(match.getBoard());

	}
}
