package backend.domain.logic.oauth2;

//@Service
//@RequiredArgsConstructor
//public class OAuth2SuccessHandler implements ServerAuthenticationSuccessHandler {
//
//    private final JwtUtil jwtUtil;
//    private final UserService userService;
//
//    @Override
//    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
//        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
//
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oAuth2User.getAttribute("email"); // Google/Facebook повертають email
//
//        if (email == null) {
//            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email не знайдено"));
//        }
//
//        // Знаходимо або створюємо користувача
//        return userService.findByEmail(email)
//                .switchIfEmpty(userService.create(new RegisterUserDto("firstname", "lastname", email, "0975258771", "password")))
//                .flatMap(user -> {
//                    // Генеруємо токени
//                    String accessToken = jwtUtil.generateAccessToken(email);
//                    String refreshToken = jwtUtil.generateRefreshToken(email);
//
//                    // Додаємо токени у відповідь
//                    response.getHeaders().add("Authorization", "Bearer " + accessToken);
//                    response.getHeaders().add("Refresh-Token", refreshToken);
//                    response.setStatusCode(HttpStatus.FOUND);
//                    response.getHeaders().setLocation(URI.create("http://localhost:4200/")); // редірект на Angular
//
//                    return response.setComplete();
//                });
//    }
//}
