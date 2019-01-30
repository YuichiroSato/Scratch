#![feature(plugin, decl_macro)]
#![feature(custom_derive)]
#![plugin(rocket_codegen)]

extern crate chat;
extern crate diesel;
extern crate rocket;
extern crate rocket_contrib;
#[macro_use] extern crate serde_derive;

pub mod view_models;

use self::chat::*;
use self::view_models::*;
use diesel::pg::PgConnection;
use rocket::request::Form;
use rocket_contrib::Template;

#[derive(FromForm)]
struct PostData {
    user_name: String,
    body: String
}

fn render_template(_connection: &PgConnection, _user_name: &str) -> Template {
    let posts = get_posts(&_connection, 100);
    let context = create_index_view_model(_user_name, posts);
    Template::render("index", &context)
}

#[get("/")]
fn index() -> Template {
    let _connection = establish_connection();
    render_template(&_connection, "")
}

#[post("/", data = "<form>")]
fn post(form: Form<PostData>) -> Template {
    let _connection = establish_connection();
    create_post(&_connection, &form.get().user_name, &form.get().body);
    render_template(&_connection, &form.get().user_name)
}

#[post("/delete", data = "<form>")]
fn delete(form: Form<PostData>) -> Template {
    let _connection = establish_connection();
    delete_posts(&_connection);
    render_template(&_connection, &form.get().user_name)
}

fn main() {
    rocket::ignite()
        .mount("/", routes![index, post, delete])
        .attach(Template::fairing())
        .launch();
}
