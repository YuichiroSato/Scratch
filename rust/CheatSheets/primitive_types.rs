fn main() {
    let x: i8 = 127;
    let y: i8 = -128;
    let z: i8 = 128;
    let w: i8 = -129;
    println!("{} {} {} {}", x, y, z, w);

    let x: i16 = 32767;
    let y: i16 = -32768;
    let z: i16 = 32768;
    let w: i16 = -32769;
    println!("{} {} {} {}", x, y, z, w);

    let x: i32 = 2_147_483_647;
    let y: i32 = -2_147_483_648;
    let z: i32 = 2_147_483_648;
    let w: i32 = -2_147_483_649;
    println!("{} {} {} {}", x, y, z, w);

    let x: i64 = 9_223_372_036_854_775_807;
    let y: i64 = -9_223_372_036_854_775_808;
    let z: i64 = 9_223_372_036_854_775_808;
    let w: i64 = -9_223_372_036_854_775_809;
    println!("{} {} {} {}", x, y, z, w);

    let x: u8 = 255;
    let y: u8 = 256;
    println!("{} {}", x, y);

    let x: u16 = 65535;
    let y: u16 = 65536;
    println!("{} {}", x, y);

    let x: u32 = 4294967295;
    let y: u32 = 4294967296;
    println!("{} {}", x, y);

    let x: u64 = 18_446_744_073_709_551_615;
    let y: u64 = 18_446_744_073_709_551_616;
    println!("{} {}", x, y);

    let x: isize = 0;
    let y: usize = 0;
    println!("{} {}", x, y);

    let decimal = 1_000;
    let hex = 0xff;
    let octal = 0o77;
    let binary = 0b1111_0000;
    let byte = b'a';
    println!("{} {} {} {} {}", decimal, hex, octal, binary, byte);
}
