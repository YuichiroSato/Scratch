import * as wasm from "hello-world";

const button = document.getElementById("button1");

button.addEventListener("click", event => {
 wasm.start();
});

const button2 = document.getElementById("button2");

button2.addEventListener("click", event => {
  const list = document.getElementById("list");
  wasm.fetch_repository(list);
});
