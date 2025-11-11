//
//  ContentView.swift
//  OnTheGoBingo
//
//  Created by Jeff Cunningham on 11/10/25.
//

import SwiftUI
import ComposeApp

struct ContentView: View {
    var body: some View {
        ComposeView()
            .ignoresSafeArea(.keyboard) // Compose has its own keyboard handling
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController() // hello
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

#Preview {
    ContentView()
}
