extern crate chrono;

use chat::models::*;
use self::chrono::{DateTime, Utc};
use std::vec::Vec;

#[derive(Serialize)]
struct PostViewModel {
    user_name: String,
    body: String,
    posted_time: String,
}

impl PostViewModel {
    fn new(post: Post) -> PostViewModel {
        let utc: DateTime<Utc> = DateTime::from(post.posted_time);
        PostViewModel {
            user_name: post.user_name,
            body: post.body,
            posted_time: utc.format("%Y-%m-%d %H:%M:%S").to_string(),
        }
    }
}

#[derive(Serialize)]
pub struct IndexViewModel {
    user_name_input: String,
    posts: Vec<PostViewModel>,
}

pub fn create_index_view_model(_user_name_input: &str, _posts: Vec<Post>) -> IndexViewModel {
    let mut post_view_model = Vec::new();
    for post in _posts {
        post_view_model.push(PostViewModel::new(post));
    }
    IndexViewModel {
        user_name_input: _user_name_input.to_string(),
        posts: post_view_model,
    }
}
