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
            .ignoresSafeArea(.keyboard)
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.initializeSession()

        let window = UIApplication.shared.connectedScenes
                    .compactMap { $0 as? UIWindowScene }
                    .flatMap { $0.windows }
                    .first { $0.isKeyWindow }

        let appleSignInHandler = AppleSignInHandler(presentationAnchor: window ?? UIWindow())


        return MainViewControllerKt.MainViewController(onSignInRequested: {
                    appleSignInHandler.startSignIn()
                })
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

#Preview {
    ContentView()
}
