use ws::{
    Handler,
    Message,
    Result,
    Sender,
};

use rustc_serialize::json::{
    encode,
    decode,
    DecodeResult,
};
use super::redis_connection::RedisConnection;

#[derive(RustcDecodable, RustcEncodable)]
enum GamePlayer {
    Player1,
    Player2,
    Watcher,
}

#[derive(RustcDecodable, RustcEncodable)]
enum GameCommand {
    Join,
    Move,
    Nope,
}

#[derive(RustcDecodable, RustcEncodable)]
struct GameMessage {
    player: GamePlayer,
    command: GameCommand,
    value: String,
}

pub struct Server {
    sender: Sender,
    redis_connection: RedisConnection,
}

impl Handler for Server {
    fn on_message(&mut self, msg: Message) -> Result<()> {
        let message = match msg.as_text() {
            Ok(s) => s,
            Err(_) => "",
        };

        let decode_result: DecodeResult<GameMessage> = decode(&message);
        match decode_result {
            Ok(game_message) => {
                Server::input_game_message(&self, game_message);
            },
            Err(_) => {},
        };

        let game_state = self.redis_connection.get_game_state();
        let json_game_state = encode(&game_state).unwrap();
        self.sender.send(json_game_state)
    }
}

impl Server {
    pub fn new(sender: Sender, redis_connection: RedisConnection) -> Server {
        Server {
            sender: sender,
            redis_connection: redis_connection,
        }
    }

    fn input_game_message(&self, game_message: GameMessage) {
        match game_message.command {
            GameCommand::Join => {
                match game_message.player {
                    GamePlayer::Player1 => {
                        self.redis_connection.set_player1_online();
                    },
                    GamePlayer::Player2 => {
                        self.redis_connection.set_player2_online();
                    },
                    GamePlayer::Watcher => {},
                }
            },
            GameCommand::Move => {
                match game_message.player {
                    GamePlayer::Player1 => {
                        let plate1 = self.redis_connection.get_plate1();
                        if game_message.value == "left" {
                            if plate1 > 10 {
                                self.redis_connection.decrby_plate1();
                            }
                        } else if game_message.value == "right" {
                            if plate1 < 100 {
                                self.redis_connection.incrby_plate1();
                            }
                        }
                    },
                    GamePlayer::Player2 => {
                        let plate2 = self.redis_connection.get_plate2();
                        if game_message.value == "left" {
                            if plate2 > 10 {
                                self.redis_connection.decrby_plate2();
                            }
                        } else if game_message.value == "right" {
                            if plate2 < 100 {
                                self.redis_connection.incrby_plate2();
                            }
                        }
                    },
                    GamePlayer::Watcher => {},
                }
            },
            GameCommand::Nope => {},
        }
    }
}
