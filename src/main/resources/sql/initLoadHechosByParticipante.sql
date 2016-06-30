SELECT
       ins1_.INSTRUMENTO as INSTRUMENTO,
       this_.FOLIO as FOLIO,
       oper6_.CLAVE as ID_OPERACION,
       this_.VOLUMEN as VOLUME,
       this_.HORA_ACEPTA_CANC_MODIF as ACCEPT_DATE,
       this_.HORA_SOLICITA_CANC_MODIF as REQUEST_DATE,
       this_.ID_USUARIO_ACEPTA_CANCEL_MODIF as ACCEPTOR,
       this_.ID_USUARIO_SOLICITA_CANC_MODIF as REQUESTER,
       merc_.CLAVE_MERCADO as MARKET,
       con7_.CLAVE AS CONCERTACION
FROM MCN_THECHOS this_
       inner join MCN_TINSTRUMENTOS ins1_
         on this_.ID_NUM_INSTRUMENTO = ins1_.ID_NUM_INSTRUMENTO
       inner join MCN_TGRUPO_NEG_INSTRUMENTOS gni2_
         on ins1_.ID_NUM_INSTRUMENTO = gni2_.ID_NUM_INSTRUMENTO
       inner join MCN_TGRUPOS_NEGOCIACION gn3_
         on gni2_.ID_GRUPO_NEGOCIACION = gn3_.ID_GRUPO_NEGOCIACION
       inner join ADMIN_MCN.MCN_CMERCADOS merc_
         on gn3_.ID_MERCADO = merc_.ID_MERCADO
       inner join MCN_TEMISORAS emi4_
         on ins1_.ID_EMISORA = emi4_.ID_EMISORA
       inner join COM_CTIPOS_VALORES tval5_
         on ins1_.ID_TIPO_VALOR = tval5_.ID_TIPO_VALOR
       inner join MCN_TENTIDADES vende9_
         on this_.ID_ENTIDAD_VENDE = vende9_.ID_ENTIDAD
       inner join MCN_TENTIDADES compra8_
         on this_.ID_ENTIDAD_COMPRA = compra8_.ID_ENTIDAD
       left outer join COM_COPERACIONES oper6_
         on this_.ID_OPERACION = oper6_.ID_OPERACION
       left outer join COM_CCONCERTACIONES con7_
         on this_.ID_CONCERTACION = con7_.ID_CONCERTACION
WHERE this_.ESTADO in (1,  2)
       and this_.id_cancelacion_masiva is null
       and tval5_.ID_BOLSA = 101
       and this_.HORA_HECHO between :horaInicial and :horaFinal