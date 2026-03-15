//
//  AppleSignInHandler.swift
//  OnTheGoBingo
//

import AuthenticationServices
import ComposeApp

class AppleSignInHandler: NSObject, ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding {

    private weak var presentationAnchor: ASPresentationAnchor?

    init(presentationAnchor: ASPresentationAnchor) {
        self.presentationAnchor = presentationAnchor
    }

    func startSignIn() {
        let request = ASAuthorizationAppleIDProvider().createRequest()
        request.requestedScopes = [.fullName, .email]

        let controller = ASAuthorizationController(authorizationRequests: [request])
        controller.delegate = self
        controller.presentationContextProvider = self
        controller.performRequests()
    }

    // MARK: - ASAuthorizationControllerDelegate

    func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization authorization: ASAuthorization
    ) {
        guard
            let credential = authorization.credential as? ASAuthorizationAppleIDCredential,
            let tokenData = credential.identityToken,
            let idToken = String(data: tokenData, encoding: .utf8)
        else {
            print("AppleSignInHandler: failed to extract identity token")
            return
        }

        let authRepository = IosApp.shared.appComponent.authRepository

        Task {
            do {
                let result = try await authRepository.signIn(
                    oAuthData: OAuthData(idToken: idToken, provider: OauthProvider.APPLE)
                )
                if result.isSuccess {
                    IosApp.shared.authState.value = IosAuthState.signedIn
                } else {
                    print("AppleSignInHandler: sign in failed")
                    IosApp.shared.authState.value = IosAuthState.signedOut
                }
            } catch {
                print("AppleSignInHandler: error - \(error)")
                IosApp.shared.authState.value = IosAuthState.signedOut
            }
        }
    }

    func authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError error: Error
    ) {
        print("AppleSignInHandler: authorization error - \(error)")
        IosApp.shared.authState.value = IosAuthState.signedOut
    }

    // MARK: - ASAuthorizationControllerPresentationContextProviding

    func presentationAnchor(for controller: ASAuthorizationController) -> ASPresentationAnchor {
        return presentationAnchor ?? UIWindow()
    }
}
