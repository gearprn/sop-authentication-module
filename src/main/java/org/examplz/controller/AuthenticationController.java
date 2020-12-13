package org.examplz.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.examplz.bean.SignInBody;
import org.examplz.model.Account;
import org.examplz.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.util.Calendar;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    private final AccountRepository accountRepository;
    Key key;

    public AuthenticationController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @RequestMapping(value = "/signIn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signIn(@RequestBody SignInBody body) {
        try {
            List<Account> accounts = accountRepository.findAccountByUsername(body.getUsername());

            // find account by username
            if (accounts.size() > 0) {
                Account account = accounts.get(0);

                // if username and password match
                if (account.getUsername().equals(body.getUsername()) && account.getPassword().equals(body.getPassword())) {

                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.SECOND, 30);

                    String jws = Jwts.builder()
                            .claim("uid", account.getUid())
                            .claim("username", account.getUsername())
                            .claim("role", account.getRole())
                            .setExpiration(c.getTime())
                            .signWith(this.key)
                            .compact();

                    return new ResponseEntity("Bearer " + jws, HttpStatus.OK);
                } else {
                    return new ResponseEntity("ชื่อผู้ใช้งานหรือรหัสผ่านไม่ถูกต้อง", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity("บัญชีนี้ไม่มีอยู่ในระบบ", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/token/validate", method = RequestMethod.GET)
    public ResponseEntity validateToken(@RequestHeader(value = "authorization") String jwsToken) {
        try {
            // check if token is Bearer type
            if (jwsToken.startsWith("Bearer ")) {
                // remove `Bearer ` from jwsToken
                jwsToken = jwsToken.replaceFirst("Bearer ", "");
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(this.key)
                        .build()
                        .parseClaimsJws(jwsToken)
                        .getBody();

                return new ResponseEntity(claims, HttpStatus.OK);
            } else {
                throw  new SignatureException("Not a Bearer token");
            }
        } catch (ExpiredJwtException e) {
            System.out.println(e);
            return new ResponseEntity("Token expired", HttpStatus.UNAUTHORIZED);
        } catch (SignatureException e) {
            System.out.println(e);
            return new ResponseEntity("Invalid token", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
        }
    }
}
