use redis:: {
    Commands,
    Connection,
};

pub struct RedisConnection {
    connection: Connection,
}

#[derive(RustcDecodable, RustcEncodable)]
pub struct GameState {
    is_player1_online: bool,
    is_player2_online: bool,
    plate1: i32,
    plate2: i32,
    ballx: i32,
    bally: i32,
    ballvx: i32,
    ballvy: i32,
}

impl RedisConnection {
    pub fn new() -> RedisConnection {
        RedisConnection {
            connection: redis::Client::open("redis://127.0.0.1/")
                .expect("Redis connection open Error")
                .get_connection()
                .expect("Redis connection open Error")
        }
    }

    pub fn set_player1_online(&self) {
        let _: () = self.connection.set("player1", "online").unwrap();
    }

    pub fn set_player1_offline(&self) {
        let _: () = self.connection.set("player1", "").unwrap();
    }

    pub fn is_player1_online(&self) -> bool {
        let p1: String = self.connection.get("player1").unwrap();
        p1 == "online"
    }

    pub fn set_player2_online(&self) {
        let _: () = self.connection.set("player2", "online").unwrap();
    }

    pub fn set_player2_offline(&self) {
        let _: () = self.connection.set("player2", "").unwrap();
    }

    pub fn is_player2_online(&self) -> bool {
        let p2: String = self.connection.get("player2").unwrap();
        p2 == "online"
    }

    pub fn set_plate1(&self, p1: i32) {
        let _: () = self.connection.set("plate1", p1).unwrap();
    }

    pub fn get_plate1(&self) -> i32 {
        let i: i32 = self.connection.get("plate1").unwrap();
        i
    }

    pub fn incrby_plate1(&self) {
        let _: () = redis::cmd("INCRBY")
            .arg("plate1")
            .arg(10)
            .query(&self.connection)
            .unwrap();
    }

    pub fn decrby_plate1(&self) {
        let _: () = redis::cmd("DECRBY")
            .arg("plate1")
            .arg(10)
            .query(&self.connection)
            .unwrap();
    }

    pub fn set_plate2(&self, p2: i32) {
        let _: () = self.connection.set("plate2", p2).unwrap();
    }

    pub fn get_plate2(&self) -> i32 {
        let i: i32 = self.connection.get("plate2").unwrap();
        i
    }

    pub fn incrby_plate2(&self) {
        let _: () = redis::cmd("INCRBY")
            .arg("plate2")
            .arg(10)
            .query(&self.connection)
            .unwrap();
    }

    pub fn decrby_plate2(&self) {
        let _: () = redis::cmd("DECRBY")
            .arg("plate2")
            .arg(10)
            .query(&self.connection)
            .unwrap();
    }


    pub fn set_ballx(&self, x: i32) {
        let _: () = self.connection.set("ballx", x).unwrap();
    }

    pub fn get_ballx(&self) -> i32 {
        let x: i32 = self.connection.get("ballx").unwrap();
        x
    }

    pub fn set_bally(&self, y: i32) {
        let _: () = self.connection.set("bally", y).unwrap();
    }

    pub fn get_bally(&self) -> i32 {
        let y: i32 = self.connection.get("bally").unwrap();
        y
    }

    pub fn set_ballvx(&self, vx: i32) {
        let _: () = self.connection.set("ballvx", vx).unwrap();
    }

    pub fn get_ballvx(&self) -> i32 {
        let vx: i32 = self.connection.get("ballvx").unwrap();
        vx
    }

    pub fn set_ballvy(&self, vy: i32) {
        let _: () = self.connection.set("ballvy", vy).unwrap();
    }

    pub fn get_ballvy(&self) -> i32 {
        let vy: i32 = self.connection.get("ballvy").unwrap();
        vy
    }

    fn start_game(&self) {
        let _: () = self.connection.set("game", "start").unwrap();
    }

    fn end_game(&self) {
        let _: () = self.connection.set("game", "").unwrap();
    }

    fn is_game_started(&self) -> bool {
        let game: String = self.connection.get("game").unwrap();
        game == "start"
    }

    pub fn init(&self) {
        self.set_player1_offline();
        self.set_player2_offline();
        self.set_plate1(50);
        self.set_plate2(50);
        self.set_ballx(50);
        self.set_bally(50);
        self.set_ballvx(0);
        self.set_ballvy(0);
        self.end_game();
    }

    pub fn get_game_state(&self) -> GameState {
        GameState {
            is_player1_online: self.is_player1_online(),
            is_player2_online: self.is_player2_online(),
            plate1: self.get_plate1(),
            plate2: self.get_plate2(),
            ballx: self.get_ballx(),
            bally: self.get_bally(),
            ballvx: self.get_ballvx(),
            ballvy: self.get_ballvy(),
        }
    }

    pub fn evolve_game_state(&self) {
        let is_game_started = self.is_game_started();
        if !is_game_started {
            let is_player1_online = self.is_player1_online();
            let is_player2_online = self.is_player2_online();
            if is_player1_online && is_player2_online {
                self.set_ballvx(3);
                self.set_ballvy(5);
                self.start_game();
            }
        } else {
            let x = self.get_ballx();
            let y = self.get_bally();
            let plate1 = self.get_plate1();
            let plate2 = self.get_plate2();
            let vx = self.get_ballvx();
            let vy = self.get_ballvy();

            let mut next_x = x + vx;
            let mut next_y = y + vy;
            let mut next_vx = vx;
            let mut next_vy = vy;

            if next_x < 0 {
                next_x = 1;
                next_vx = -next_vx;
            }
            if next_x > 100 {
                next_x = 99;
                next_vx = -next_vx;
            }
            // bounce by plate1
            if y >= 10 && next_y < 10 && x <= plate1 && x >= plate1 - 10 {
                next_y = 11;
                next_vy = -next_vy;
            }
            // bounce by plate2
            if y <= 90 && next_y > 90 && x <= plate2 && x >= plate2 - 10 {
                next_y = 89;
                next_vy = -next_vy;
            }

            // Game over
            if next_y < 10 || next_y > 90 {
                self.init();
                return;
            }

            self.set_ballx(next_x);
            self.set_bally(next_y);
            self.set_ballvx(next_vx);
            self.set_ballvy(next_vy);
        }
    }
}
