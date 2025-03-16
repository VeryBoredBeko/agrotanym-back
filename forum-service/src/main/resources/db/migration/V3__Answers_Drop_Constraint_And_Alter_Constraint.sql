alter table forum.answers
drop constraint answers_question_id_fk;

alter table forum.answers
drop constraint answers_question_id_fkey;

alter table forum.answers
add constraint answers_question_id_fkey
foreign key (question_id) references forum.questions(id)
on delete cascade;
