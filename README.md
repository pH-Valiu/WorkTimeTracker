```
Commands available:
  help:    		         Shows the help page
  start:               Starts the time tracking
  stop:                Stops the time tracking
  month <maxHours>     Displays the stats of the current month. You can optionally specify the needed amount of worked hours (default is 40). 
  week <maxHours>      Display the stats of the current week. You can optionally specify the needed amount of worked hours (default is 10). 

Output is normally colored...
Example output of 'month':
-----------------------------------------Work Time Tracker---------------------------------------

Total work load for Mai:                        40h
Already worked off:                             30h 34min
Still have to work off:                         09h 25min
Weekly Distribution:
        1.      |       2.      |       3.      |       4.      |       5       |       6       |
    09h 00min   |    21h 34min  |    ---------  |    ---------  |    ---------  |    ---------  | <- # Accomplished, # Missed
 of 06h 27min   | of 09h 01min  | of 09h 01min  | of 09h 01min  | of 06h 27min  |    ---------  | <- Optimal Distribution

Weekly distribution for remaining work:
        1.      |       2.      |       3.      |       4.      |       5       |       6       |
    ---------   |    02h 32min  |    02h 32min  |    02h 32min  |    01h 48min  |    ---------  |

-------------------------------------------------------------------------------------------------


Example output of 'week':
-----------------------------------------Work Time Tracker-----------------------------------------------------------------------------

Total work load for Mai, Week 2:                30h
Already worked off:                             21h 34min
Still have to work off:                         08h 25min
Daily Distribution:
───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
00:00 |       |     00:00 |       |     00:00 | ▒▒▒▒▒ |     00:00 |       |     00:00 |       |     00:00 |       |     00:00 |       |
      |       |           |       |           | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |
02:00 |       |     02:00 |       |     02:00 | ▒▒▒▒▒ |     02:00 |       |     02:00 |       |     02:00 |       |     02:00 |       |
      |       |           |       |           | ░░░░░ |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
11:00 | ▒▒▒▒▒ |     11:00 |       |     11:00 |       |     11:00 |       |     11:00 |       |     11:00 |       |     11:00 |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
      | ░░░░░ |           |       |           |       |           |       |           |       |           |       |           |       |
14:00 | ░░░░░ |     14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
17:00 | ▒▒▒▒▒ |     17:00 |       |     17:00 |       |     17:00 |       |     17:00 |       |     17:00 |       |     17:00 |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
20:00 | ▒▒▒▒▒ |     20:00 | ░░░░░ |     20:00 |       |     20:00 |       |     20:00 |       |     20:00 |       |     20:00 |       |
      |       |           | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |
      |       |           | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           | ▒▒▒▒▒ |
23:00 |       |     23:00 | ▒▒▒▒▒ |     23:00 |       |     23:00 |       |     23:00 |       |     23:00 |       |     23:00 | ▒▒▒▒▒ |
         Mon.                Tue.                Wed.                Thu.                Fri.                Sat.                Sun.
───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────


         Mon.       |        Tue.       |        Wed.       |        Thu.       |        Fri.       |        Sat.       |        Sun.       |      
      08h 34min     |     06h 41min     |     ---------     |     ---------     |     ---------     |     ---------     |     06h 18min     | <- # Accomplished, # Missed
   of 04h 17min     |  of 04h 17min     |  of 04h 17min     |  of 04h 17min     |  of 04h 17min     |  of 04h 17min     |  of 04h 17min     | <- Optimal Distribution

Daily distribution for remaining work:
         Mon.       |        Tue.       |        Wed.       |        Thu.       |        Fri.       |        Sat.       |        Sun.       |      
      01h 24min     |     01h 24min     |     01h 24min     |     01h 24min     |     01h 24min     |     01h 24min     |     ---------     |      

---------------------------------------------------------------------------------------------------------------------------------------------  
