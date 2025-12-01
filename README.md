# ScenarioQualityChecker

## Opis aplikacji
Dla analityków dokumentujących wymagania funkcjonalne za pomocą scenariuszy nasza aplikacja SQC dostarczy informacji ilościowych oraz umożliwi wykrywanie problemów w wymaganiach funkcjonalnych zapisanych w postaci scenariuszy. Aplikacja będzie dostępna poprzez GUI a także jako zdalne API dzięki czemu można ją zintegrować z istniejącymi narzędziami.

### Zasady punktowania
https://docs.google.com/spreadsheets/d/e/2PACX-1vTn6j3M8pmGEzrsQk8mXse7lVHUdhYWkfxbkQiYI23rBtwM4N3bWw0qtupW-gesfCkcYasnZ-eEXl-F/pubhtml

## Notacja scenariuszy
- Scenariusz zawiera nagłówek określający jego tytuł i aktorów (zewnętrznych oraz system)
- Scenariusz składa się z kroków (każdy krok zawiera tekst)
- Kroki mogą zawierać pod-scenariusze (dowolny poziom zagłębień)
- Kroki mogą się zaczynać od słów kluczowych: IF, ELSE, FOR EACH

### Przykład:
Tytuł: Dodanie książki\
Aktorzy:  Bibliotekarz\
Aktor systemowy: System\

* Bibliotekarz wybiera opcje dodania nowej pozycji książkowej
* Wyświetla się formularz.
* Bibliotekarz podaje dane książki.
* IF: Bibliotekarz pragnie dodać egzemplarze książki
  - Bibliotekarz wybiera opcję definiowania egzemplarzy
  - System prezentuje zdefiniowane egzemplarze
  - FOR EACH egzemplarz:
      * Bibliotekarz wybiera opcję dodania egzemplarza
      * System prosi o podanie danych egzemplarza
      * Bibliotekarz podaje dane egzemplarza i zatwierdza.
      * System informuje o poprawnym dodaniu egzemplarza i prezentuje zaktualizowaną listę egzemplarzy.
* Bibliotekarz zatwierdza dodanie książki.
* System informuje o poprawnym dodaniu książki.
  
## Autorzy
- Dominik Durlik 16011
- Jakub Biernat 160248
- Zofia Kapcińska 160227
- Eryk Masian 160228
