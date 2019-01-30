fn main() {
    let mut v = vec![1, 2, 3];
    v.push(4);
    println!("{:?}", v);

    {
        let first = &v[0];
        println!("{}", first);
        // error!
        // v.push(5);
    }
    v.push(5);
    println!("{:?}", v);

    for i in &mut v {
        *i += 1;
    }
    for i in &v {
        println!("{}", i);
    }
}
