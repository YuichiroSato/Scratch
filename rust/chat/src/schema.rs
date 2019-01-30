table! {
    posts (post_id) {
        post_id -> Integer,
        user_name -> Text,
        body -> Text,
        posted_time -> Timestamp,
        is_deleted -> Bool,
    }
}
