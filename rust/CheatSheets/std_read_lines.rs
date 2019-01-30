use std::io::*;

fn main() {
    let mut buf = String::new();
    stdin().read_to_string(&mut buf).unwrap();
    let mut lines = buf.lines();

    let first = lines.next().unwrap().to_string();
    let second = lines.next().unwrap().to_string();
    let mut splitted = second.split_whitespace();
    let second_head = splitted.next().unwrap().to_string();
    let second_tail = splitted.next().unwrap().to_string();
    println!("{} {} {}", first, second_head, second_tail);
}
