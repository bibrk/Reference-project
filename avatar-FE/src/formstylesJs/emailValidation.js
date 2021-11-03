function emailIsValid (email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
  }
  
  emailIsValid('tyler@tyler@ui.dev') 
  emailIsValid('tyler@ui.dev')