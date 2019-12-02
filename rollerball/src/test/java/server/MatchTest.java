package server;

import org.junit.Assert;
import org.junit.Test;

import server.matches.Match;
import server.api.MoveRequest;
import server.api.MoveResponse;

public class MatchTest {
	@Test
	public void testMoves() {
		int whiteId = 255;
		int blackId = 25*71;
		Match match = Match.createNewMatch(15, whiteId, blackId);
		MoveRequest request = new MoveRequest();
		MoveResponse response = new MoveResponse();
		
		request.from = "garbage";
		request.to = "a4";

		response = match.move(request);
		Assert.assertTrue(response.success == false);

		request.from = "c2";
		request.to = "garbage";
		
		response = match.move(request);
		Assert.assertTrue(response.success == false);

		request.from = "c2";
		request.to = "b3";
		
		response = match.move(request);
		Assert.assertTrue(response.success == true);
	}
}
