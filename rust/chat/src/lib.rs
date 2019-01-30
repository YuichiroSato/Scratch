pub mod schema;
pub mod models;

#[macro_use]
extern crate diesel;
extern crate dotenv;

use diesel::prelude::*;
use diesel::pg::PgConnection;
use diesel::expression::dsl::sql;
use diesel::types::Bool;
use dotenv::dotenv;
use std::env;

use self::models::*;
use self::schema::posts::dsl::*;
use self::diesel::prelude::*;

pub fn establish_connection() -> PgConnection {
    dotenv().ok();

    let database_url = env::var("DATABASE_URL")
        .expect("DATABASE_URL must be set");
    PgConnection::establish(&database_url)
        .expect(&format!("Error connecting to {}", database_url))
}

pub fn get_posts(_connection: &PgConnection, n: i64) -> Vec<Post> {
    let _connection = establish_connection();
    let results = posts.filter(is_deleted.eq(false))
        .order(posted_time.desc())
        .limit(n)
        .load::<Post>(&_connection)
        .expect("Error loading posts");
    results
}

pub fn create_post(_connection: &PgConnection, user_name_str: &String, body_str: &String) -> Post {
    use schema::posts;

    let new_post = NewPost {
        user_name: user_name_str,
        body: body_str,
    };

    diesel::insert_into(posts::table)
        .values(&new_post)
        .get_result(_connection)
        .expect("Error inserting new post")
}

pub fn delete_posts(_connection: &PgConnection) {
    sql::<Bool>("UPDATE posts SET is_deleted = true")
        .execute(_connection)
        .expect("Error deleting posts");
    ()
}
