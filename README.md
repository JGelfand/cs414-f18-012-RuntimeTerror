# cs414-f19-012-RuntimeTerror
CS 414 group project repo

![CI status](https://github.com/JGelfand/cs414-f19-012-RuntimeTerror/workflows/CI/badge.svg)

Installation:
1. Clone the repository (`git clone https://github.com/JGelfand/cs414-f19-012-RuntimeTerror.git`) and cd in
2. `cd rollerball`
3. `npm install --prefix client`
4. Create a file in rollerball/src/main/resources/sql_login_info.txt, and in it put the username, some whitespace, and then the password for the account you wish to use to log in to the mysql database

Build/Run:
1. `npm run bundle --prefix client`
2. `gradle run`
