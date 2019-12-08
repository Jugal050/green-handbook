-- table structure:  
SELECT  
    lower(table_name) table_name,  
    TEMPORARY,  
    tablespace_name,  
    num_rows,  
    duration,  
    'ORACLE' table_type,  
    partitioned,  
    (  
        SELECT  
            ceil(sum(bytes) / 1024 / 1024)  
        FROM  
            dba_segments b  
        WHERE  
            a. OWNER = b. OWNER  
        AND a.table_name = b.segment_name  
    ) AS store_capacity  
FROM  
    dba_tables a  
WHERE  
    OWNER = ?  
AND table_name NOT LIKE 'TMP%';  
   
SELECT  
    lower(column_name) column_name,  
    column_id position,  
    data_type,  
    data_length,  
    data_precision,  
    data_scale,  
    nullable,  
    data_default default_value,  
    default_length  
FROM  
    dba_tab_columns  
WHERE  
    OWNER = ?  
AND table_name = ?;  
   
-- index  
SELECT  
    lower(index_name) index_name,  
    index_type type  
FROM  
    dba_indexes  
WHERE  
    OWNER = ?  
AND table_name = ?  
AND index_name NOT LIKE 'SYS_IL%';  
   
SELECT  
    lower(column_name) column_name,  
    column_position,  
    descend  
FROM  
    dba_ind_columns  
WHERE  
    table_owner = ?  
AND table_name = ?  
AND index_name = ?;  
   
-- collect description  
SELECT  
    comments  
FROM  
    dba_tab_comments  
WHERE  
    OWNER = ?  
AND table_name = ?;  
   
SELECT  
    lower(column_name) column_name,  
    comments  
FROM  
    dba_col_comments  
WHERE  
    OWNER = ?  
AND table_name = ?;  
   
-- database  
SELECT  
    lower(username) username  
FROM  
    dba_users  
WHERE  
    username NOT IN (  
        'STDBYPERF',  
        'READONLY',  
        'APPQOSSYS',  
        'ANYSQL',  
        'DBFLASH',  
        'SYS',  
        'SYSTEM',  
        'MONITOR',  
        'TBSEARCH',  
        'MANAGER',  
        'SYSMAN',  
        'EXFSYS',  
        'WMSYS',  
        'DIP',  
        'TSMSYS',  
        'ORACLE_OCM',  
        'OUTLN',  
        'DBSNMP',  
        'PERFSTAT',  
        'SEARCH',  
        'TOOLS',  
        'TBDUMP',  
        'DMSYS',  
        'XDB',  
        'ANONYMOUS',  
        'DEV_DDL'  
    );  
   
--segsize  
SELECT  
    round(sum(bytes) / 1024 / 1024, 0) mbytes  
FROM  
    dba_segments  
WHERE  
    OWNER = ?  
AND segment_name = ?;  