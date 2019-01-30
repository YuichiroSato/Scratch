fn main() {
    let str = String::from("あいうえお");
    let sub_str = &str[3..6];
    println!("{}", sub_str);

    let s1 = String::from("Hello ");
    let s2 = String::from("world!");
    let s3 = s1 + &s2;
    // error!
    // println!("{}", s1);
    println!("{}", s3);

    let s1 = String::from("Hello");
    let s2 = String::from("world");
    println!("{}", format!("{} {}!", s1, s2));
}
