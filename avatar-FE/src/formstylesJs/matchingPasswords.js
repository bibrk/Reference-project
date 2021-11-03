function checkPassword(form) {
    password1 = form.password1.value;
    password2 = form.password2.value;

    if (password1 == '')
        alert ('Please enter password!');
          
    else if (password2 == '')
        alert ('Please confirm your password!');
             
    else if (password1 != password2) {
        alert ('\nPassword did not match! Please try again...')
        return false;
    }


}