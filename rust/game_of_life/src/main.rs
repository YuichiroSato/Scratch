extern crate gtk;
#[macro_use] extern crate relm;
#[macro_use] extern crate relm_derive;

use gtk:: {
    GtkWindowExt,
    WidgetExt,
    Window,
    WindowType,
};
use relm::{Relm, Update, Widget};

use self::Msg::*;

#[derive(Msg)]
enum Msg {
    Quit,
}

#[derive(Clone)]
struct Win {
    window: Window,
}

impl Update for Win {
    type Model = ();
    type ModelParam = ();
    type Msg = Msg;

    fn model(_: &Relm<Self>, _: ()) {
        ()
    }

    fn update(&mut self, event: Msg) {
        match event {
            Quit => gtk::main_quit(),
        }
    }
}

impl Widget for Win {
    type Root = Window;

    fn root(&self) -> Self::Root {
        self.window.clone()
    }

    fn view(relm: &Relm<Self>, _model: Self::Model) -> Self {
        let window = Window::new(WindowType::Toplevel);
        window.set_default_size(800, 800);
        window.set_title("Game of Life");
        window.show_all();

        let mut win = Win {
            window: window,
        };
        win
    }
}

fn main() {
    Win::run(()).expect("Win::run failed");
}
