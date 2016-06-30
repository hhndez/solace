SELECT A.ID_ETAPA_SUSPENSION, A.NIVEL, A.VARIACION, A.REANUDAR_CIERRE, A.DURACION, A.MODO_REANUDACION  , A.DURACION_SUBASTA 
FROM MCN_TNIVEL_ETAPA_SUSPENSION A, MCN_TETAPAS_SUSPENSION B  
WHERE A.ESTADO_ADMIN= 1
AND A.ID_ETAPA_SUSPENSION = B.ID_ETAPA_SUSPENSION
AND B.ESTADO_ADMIN = 1
ORDER BY A.ID_ETAPA_SUSPENSION, A.NIVEL ASC