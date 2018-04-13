DELETE FROM  mb_deposit WHERE login_name IN (
SELECT a.login_name FROM mb_customer  a LEFT JOIN (
SELECT COUNT(1) AS c,login_name FROM mb_deposit WHERE STATUS =3 AND DATE_SUB(CURDATE(), INTERVAL 180 DAY) <= create_date GROUP BY login_name)  b 
ON a.login_name = b.login_name WHERE c > 50) AND DATE_SUB(CURDATE(), INTERVAL 180 DAY) >= create_date  
ORDER BY create_date DESC;

DELETE FROM  mb_withdraw WHERE login_name IN (
SELECT a.login_name FROM mb_customer  a LEFT JOIN (
SELECT COUNT(1) AS c,login_name FROM mb_withdraw WHERE STATUS =5 AND DATE_SUB(CURDATE(), INTERVAL 180 DAY) <= create_date GROUP BY login_name)  b 
ON a.login_name = b.login_name WHERE c > 50) AND DATE_SUB(CURDATE(), INTERVAL 180 DAY) >= create_date  
ORDER BY create_date DESC;

DELETE FROM mb_cash_gift WHERE login_name IN (
SELECT a.login_name FROM mb_customer  a LEFT JOIN (
SELECT COUNT(1) AS c,login_name FROM mb_cash_gift WHERE STATUS =3 AND DATE_SUB(CURDATE(), INTERVAL 180 DAY) <= create_date GROUP BY login_name)  b 
ON a.login_name = b.login_name WHERE c > 200) AND DATE_SUB(CURDATE(), INTERVAL 180 DAY) >= create_date  
ORDER BY create_date DESC;

DELETE FROM mb_deposit_log WHERE log_id IN (SELECT t.log_id AS ld FROM (
SELECT a.log_id  FROM mb_deposit_log a LEFT JOIN mb_deposit b ON a.deposit_id =b.deposit_id WHERE b.deposit_id IS  NULL) t);

DELETE FROM mb_withdraw_log WHERE log_id IN (SELECT t.log_id AS ld FROM (
SELECT a.log_id  FROM mb_withdraw_log a LEFT JOIN mb_withdraw b ON a.withdraw_id =b.withdraw_id WHERE b.withdraw_id IS  NULL) t);