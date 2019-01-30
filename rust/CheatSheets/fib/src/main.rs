use std::time::SystemTime;

fn main() {
    let start = SystemTime::now();
    println!("{}", fib(40));
    let end = SystemTime::now();
    let duration = end.duration_since(start).expect("");
    println!("{:?}", duration)
}

fn fib(n: usize) -> usize {
    if n == 0 {
        return 0;
    } else if n == 1 {
        return 1;
    }
    return fib(n - 1) + fib(n - 2);
}
