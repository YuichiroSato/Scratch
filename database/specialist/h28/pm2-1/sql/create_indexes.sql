create index buka_index_1 on Buka(shiten_code);

create index eigyouin_index_1 on Eigyouin(buka_code);

create index unyou_syouhin_index_1 on UnyouSyouhin(syouhin_syubetu_code);

create index kokyaku_kihon_index_1 on KokyakuKihon(syokugyou_code);
create index kokyaku_kihon_index_2 on KokyakuKihon(tantou_kouin_no);

create index kokyaku_syousai_index_1 on KokyakuSyousai(kokyaku_no);

create index kokyaku_kouza_index_1 on KokyakuKouza(shiten_code);
create index kokyaku_kouza_index_2 on KokyakuKouza(kamoku_code);
create index kokyaku_kouza_index_3 on KokyakuKouza(kokyaku_no);

create index anken_index_1 on Anken(shiten_code);
create index anken_index_2 on Anken(kouin_no);
create index anken_index_3 on Anken(kokyaku_no);

create index torihiki_index_1 on Torihiki(torihiki_syubetu_code);
create index torihiki_index_2 on Torihiki(syouhin_code);
create index torihiki_index_3 on Torihiki(shiten_code, anken_no);
create index torihiki_index_4 on Torihiki(shiten_code, kamoku_code, kouza_no);

create index contact_history_index_1 on ContactHistory(shiten_code, anken_no);
create index contact_history_index_2 on ContactHistory(shiten_code);

create index schedule_index_1 on Schedule(kouin_no);
create index schedule_index_2 on Schedule(shiten_code, anken_no);
