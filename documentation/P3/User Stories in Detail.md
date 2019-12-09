User stories in depth:

The user can register an account using email, nickname, and password. Criterion: - Create account with email used in an existing account - fail - Create account with username used in an existing account - fail - Create account with password less than 8 characters - fail - Create account with username ‘admin’ - fail - Create account with username that is a url or email address - fail - Create account with unique username, nickname - pass - Receive notification after registering successfully - pass

As a user, I can log in after creating my account Criterion: - Log in with nonexistent username/email address - fail - Log in with wrong password to existing username/email address - fail - Log in with correct username/email and password - pass

As a user, I can create a match by inviting other users. Criterion: - Send invite to another player who exists - pass - Send invite to player who doesn't exist - fail - Create match when user accepts invite - pass

As a user, I can accept or reject invitations to join other users' matches. Criterion: - Receive invite from another player - pass - Reject invite from another player - pass

As a user, if I create the match I get the first move. Criterion: - Game is created and user who created the match gets the first move - pass - Game is created and user who created the match gets the second move - fail

As a user, I can send and receive messages. Criterion: - Send message to another user that exists - pass - Send message to user who doesn't exist - fail - Receive message that was sent from another user - pass

As a user, I can see all of my current matches. Criterion: - User views current matches - pass - User views completed matches - fail

As a user, I can open one of my current matches. Criterion: - User opens ongoing match - pass - User opens match that has completed - fail

As a user user I can make a legal Rollerball move if it is my turn. Criterion: - User attempts move when it is not their turn - fail - User attempts illegal move on their turn - fail - User attempts move on their turn - pass

As a user, I will receive a notification with the results of matches. Criterion: - Match ends and notification is send to both users telling them the result - pass - Match ends and no notifications are sent - fail - Match ends and only one user receives a message - fail

As a user, I can log out. Criterion: - User presses log out button and logs them out - pass - User presses log out button and are not logged out - fail

As a user, I can continue games after logging out and back in. Criterion: - User logs out of system, logs back into system and continues a game - pass - User logs out of system, logs back into system, and cannot continue games - fail - User logs in and out multiple time and is able to continue games - pass - User logs in and out multiple time and is unable to continue games - fail

The user has a user profile with -their history -that other users can view. Criterion: - user can view history of their matches - pass - user cannot view history - fail - all matches are visible on history - pass - not all matches are visible on history - fail - user can view another user's history - pass - a user is unable to view another user's history - fail
