---
"evervault-android": minor
---

fix: A bug exists with slower network connectivity that can cause an encryption to fail. This will result
in a card being marked as valid as the card number is valid. Explit empty checks are not performed before
the card is returned from the card component.
feat: implement new CardNumberField for control over onNext capability. When using a CardNumberField without
CVC or expiry expose the onNext function to control the focus behaviour of the compose component.
