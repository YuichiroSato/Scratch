fn main() {
    let t = (1, 'a');
    let (i, c) = t;
    println!("{} {}", t.0, t.1);
    println!("{} {}", i, c);

    let t = return_tuple();
    println!("{:?}", t);
}

fn return_tuple() -> (isize, usize) {
    (1, 2)
}
