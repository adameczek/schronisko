# schronisko

## Konfiguracja lokalnie
1. Sklonuj repo
2. Zainstaluj dockera
3. Ściągnij apke
  ```
docker pull ghcr.io/adameczek/schronisko:latest
```
4. Ściągnij mongo w dockerze
5. Pobierz plik .env (Napisz do mnie i Ci go udostępnie)
6. Utwórz taką strukture folderów:
```
.
  .env
  mongodb
    data
    initdb.d
      create-user.sh
    log
.
└── docker/
    ├── .env
    └── mongodb/
        ├── data
        ├── initdb.d/
        │   └── create-user.sh
        └── log
```
7. Uruchom komende (ważne żeby być w folderze głównym repo):
```
docker-compose --env-file "ŚCIEŻKA DO PLIKU .ENV" build
docker-compose --env-file "C:\studia\inzynierka\docker\.env" up -d
```
8. sprawdź czy apka działa robiąc request 
```GET localhost:25000/hello```
