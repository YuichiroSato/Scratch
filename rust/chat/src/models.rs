use std::time::SystemTime;

#[derive(Queryable)]
pub struct Post {
    pub post_id: i32,
    pub user_name: String,
    pub body: String,
    pub posted_time: SystemTime,
    pub is_deleted: bool
}

use super::schema::posts;

#[derive(Insertable)]
#[table_name="posts"]
pub struct NewPost<'a, 'b> {
    pub user_name: &'a String,
    pub body: &'b String
}
