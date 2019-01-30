fn main() {
    println!("{:?}", natural_numbers(10));
}

fn seq(first_element: i32) -> Box<FnMut() -> i32> {
    let mut n = first_element - 1;
    Box::new(move || {
        n += 1;
        n
    })
}

fn call_n_times<T>(f: &mut FnMut() -> T, n: i32) -> Vec<T> {
    (0..n).into_iter()
        .map(|_| { (*f)() })
        .collect()
}

fn natural_numbers(n: i32) -> Vec<i32> {
    let mut i = seq(1);
    call_n_times(&mut *i, n)
}
