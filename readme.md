Цель создания этого приложения - помощь аниматору в анализе движения персонажей.
Ключевой особенностью является простота управления кадрами - добавления пустых кадров и удаления ненужных кадров, с сохранением информации о сделанных изменениях.
Это поможет аниматору определиться со спейсингом и таймингом - термины, которые являются фундаментом анимации. И в дальнейшем поправить анимацию у себя в сцене основываясь на информации о сделанных изменениях.

Так как плейбласты аниматоров обычно никогда не превышают 15 секунд, то и приложение рассчитано на короткие видео(плейбласты).

В приложении используются технологии `Spring Boot` и `JavaFX`.
Поддерживаются форматы `mp4` и `mov`. Декодирование осуществлено с помощью библиотеки `Xuggler`.

На данный момент кроме вышеперечисленной возможности имеются функции:

*    проигрывания каждый n-ый кадр
    
*    зеркального отображение
    
*    выделения области проигрывания
    
*    zoom видео
    
*    drag&drop видео
    
*    undo
    
Большинство функций назначены на клавиши клавиатуры. Список функций и назначенные на них клавиши можно посмотреть нажав `F1`. Информация будет выведена в консоль.
Приложение еще находится в стадии разработке, но его уже полностью можно использовать в работе.

Архив с образцами плейбластов и секвенции кадров можно скачать [здесь](https://drive.google.com/open?id=1ZvIx35oriZtv5wtaGvU4Hx-YVghOmUvW) .
Скопируйте папку assets из архива в`resources`. Тесты UI используют папку `assets/sample` для нахождения изображений.

В дальнейшем планирую добавить возможность рисования поверх видео, функцию ghost на нарисованных фреймах и другое...