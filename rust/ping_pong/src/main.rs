#![feature(plugin, decl_macro)]
#![feature(custom_derive)]
#![plugin(rocket_codegen)]

extern crate rocket;
extern crate rocket_contrib;
extern crate rustc_serialize;
extern crate ws;

use ws::listen;
use rustc_serialize::json;
use std::thread;
use std::time::Duration;
use std::sync::{Arc, Mutex};

mod redis_connection;
mod web_server;
mod web_socket_server;

fn main() {
    redis_connection::RedisConnection::new().init();
    let mut thread_started = false;
    let mut _sync_thread = thread::spawn(|| {});
    let _server = thread::Builder::new().name("server".to_owned()).spawn(move || {
        listen("127.0.0.1:12345", move |sender| {
            if !thread_started {
                let c = redis_connection::RedisConnection::new();
                let arcc = Arc::new(Mutex::new(c));
                let arcs = Arc::new(Mutex::new(sender.clone()));
                _sync_thread = thread::spawn(move || {
                    loop {
                        arcc.lock().unwrap().evolve_game_state();
                        let game_state = arcc.lock().unwrap().get_game_state();
                        let json_game_state = json::encode(&game_state).unwrap();
                        arcs.lock().unwrap().broadcast(json_game_state).expect("Game state broadcast error");
                        thread::sleep(Duration::from_secs(1))
                    }
                });
                thread_started = true;
            }
          web_socket_server::Server::new(
              sender,
              redis_connection::RedisConnection::new()
          )
        }).unwrap()
    }).unwrap();
    web_server::init_web_server();
}
