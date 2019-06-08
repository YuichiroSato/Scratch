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

    let v2 = vec![vec![1, 2, 3, 4], vec![5, 6, 7, 8], vec![9, 10, 11, 12]];
    println!("{:?}", v2);
    assert_eq!(10, v2[2][1]);
}
