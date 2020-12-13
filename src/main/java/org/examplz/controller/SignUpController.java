package org.examplz.controller;

import org.apache.commons.validator.routines.EmailValidator;
import org.examplz.bean.*;
import org.examplz.model.Account;
import org.examplz.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SignUpController {

    private final AccountRepository accountRepository;

    public SignUpController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "/signup/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signUpUser(@RequestBody UserSignUpBody body) {
        String isValid = validateBody(body);
        if (isValid.equals("valid")) {
            List<Account> accounts = accountRepository.findAccountByUsername(body.getUsername());

            if (accounts.size() == 0) {
                Account account = new Account(body.getUsername(), body.getPassword(), "customer");
                accountRepository.save(account);

                // เราส่งข้อมูล customer ที่เหลือให้ customer module แต่เนื่องจาก service ของ customer ไม่ได้ออนไลน์อยู่เลยเก็บเป็น object ไว้เฉย ๆ
                String[] dob = body.getDateOfBirth().split("-");
                DateOfBirth dateOfBirth = new DateOfBirth(dob[0], dob[1], dob[2]);
                User customer = new User(account.getUid(), body.getEmail(), body.getFirstName(), body.getLastName(), body.getPhone(), dateOfBirth);

                return new ResponseEntity("สร้างบัญชีผู้ใช้งานใหม่เรียบร้อย", HttpStatus.OK);
            } else {
                return new ResponseEntity("ชื่อ username มีผู้ใช้งานแล้ว", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(isValid, HttpStatus.OK);
    }

    @RequestMapping(value = "/signup/shop", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity signUpShop(@RequestBody ShopSignUpBody body) {
        String isValid = validateBody(body);
        if (isValid.equals("valid")) {
            List<Account> accounts = accountRepository.findAccountByUsername(body.getUsername());

            if (accounts.size() == 0) {
                Account account = new Account(body.getUsername(), body.getPassword(), "customer");
                accountRepository.save(account);

                // เราส่งข้อมูล shop ที่เหลือให้ shop module แต่เนื่องจาก service ของ shop ไม่ได้ออนไลน์อยู่เลยเก็บเป็น object ไว้เฉย ๆ
                Shop shop = new Shop(account.getUid(), body.getEmail(), body.getFirstName(), body.getLastName(), body.getPhone(), body.getShopName(), body.getShopDetail());

                return new ResponseEntity("สร้างบัญชีร้านค้าใหม่เรียบร้อย", HttpStatus.OK);
            } else {
                return new ResponseEntity("ชื่อ username มีผู้ใช้งานแล้ว", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity(isValid, HttpStatus.OK);
    }

    private String validateBody(ShopSignUpBody body) {
        String email = body.getEmail();
        String username = body.getUsername();
        String password = body.getPassword();
        String firstName = body.getFirstName();
        String lastName = body.getLastName();
        String phone = body.getPhone();
        String shopName = body.getShopName();
        String shopDetail = body.getShopDetail();

        // email validation using Apache Commons Validator
        if (!EmailValidator.getInstance().isValid(email))
            return "อีเมลไม่ถูกต้อง";

        // username validation
        if (username.length() < 2 || username.length() > 20)
            return "ชื่อผู้ใช้งานต้องมีตัวอักษร 2 ตัว ถึง 20 ตัว";
        else if (username.equals(""))
            return "ชื่อผู้ใช้งานไม่สามารถเว้นว่างได้";

        // password validation
        if (password.length() < 8)
            return "รหัสผ่านต้องมี 8 ตัวอักษรขึ้นไป";

        // firstname validation
        if (firstName.length() < 2 || firstName.length() > 35)
            return "ชื่อจริงต้องมีตัวอักษร 2 ตัว ถึง 35 ตัว";
        else if (firstName.equals(""))
            return "ชื่อจริงไม่สามารถเว้นว่างได้";

        // lastname validation
        if (lastName.length() < 2 || lastName.length() > 35)
            return "นามสกุลต้องมีตัวอักษร 2 ตัว ถึง 35 ตัว";
        else if (lastName.equals(""))
            return "นามสกุลไม่สามารถเว้นว่างได้";

        // phone validation
        if (phone.length() < 9 || phone.length() > 10)
            return "เบอร์โทรศัพท์ต้องมีตัวเลข 9 หรือ 10 ตัว";
        else if (phone.equals(""))
            return "เบอร์โทรไม่สามารถเว้นว่างได้";

        // shop name validation
        if (shopName.length() < 9 || shopName.length() > 20)
            return "ชื่อร้านค้าต้องมีตัวอักษร 2 ตัว ถึง 20 ตัว";
        else if (shopName.equals(""))
            return "ชื่อร้านค้าไม่สามารถเว้นว่างได้";

        // shop detail validation
        if (shopDetail.length() < 9 || shopDetail.length() > 20)
            return "ชื่อร้านค้าต้องมีตัวอักษร 2 ตัว ถึง 20 ตัว";
        else if (shopDetail.equals(""))
            return "ชื่อร้านค้าไม่สามารถเว้นว่างได้";

        return "valid";
    }

    private String validateBody(UserSignUpBody body) {
        String email = body.getEmail();
        String username = body.getUsername();
        String password = body.getPassword();
        String firstName = body.getFirstName();
        String lastName = body.getLastName();
        String phone = body.getPhone();
        String[] dob = body.getDateOfBirth().split("-");

        // email validation using Apache Commons Validator
        if (!EmailValidator.getInstance().isValid(email))
            return "อีเมลไม่ถูกต้อง";

        // username validation
        if (username.length() < 2 || username.length() > 20)
            return "ชื่อผู้ใช้งานต้องมีตัวอักษร 2 ตัว ถึง 20 ตัว";
        else if (username.equals(""))
            return "ชื่อผู้ใช้งานไม่สามารถเว้นว่างได้";

        // password validation
        if (password.length() < 8)
            return "รหัสผ่านต้องมี 8 ตัวอักษรขึ้นไป";

        // firstname validation
        if (firstName.length() < 2 || firstName.length() > 35)
            return "ชื่อจริงต้องมีตัวอักษร 2 ตัว ถึง 35 ตัว";
        else if (firstName.equals(""))
            return "ชื่อจริงไม่สามารถเว้นว่างได้";

        // lastname validation
        if (lastName.length() < 2 || lastName.length() > 35)
            return "นามสกุลต้องมีตัวอักษร 2 ตัว ถึง 35 ตัว";
        else if (lastName.equals(""))
            return "นามสกุลไม่สามารถเว้นว่างได้";

        // phone validation
        if (phone.length() < 9 || phone.length() > 10)
            return "เบอร์โทรศัพท์ต้องมีตัวเลข 9 หรือ 10 ตัว";
        else if (phone.equals(""))
            return "เบอร์โทรไม่สามารถเว้นว่างได้";

        // date of birth validation
        if (dob[0].length() != 4
                && (Integer.parseInt(dob[1]) < 1 || Integer.parseInt(dob[1]) > 12)
                && (Integer.parseInt(dob[2]) < 1 || Integer.parseInt(dob[2]) > 31))
            return "วันเกิดไม่ถูกต้อง";

        return "valid";
    }
}
