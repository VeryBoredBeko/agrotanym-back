alter table forum.answers
add constraint answers_question_id_fk
foreign key (question_id) references forum.questions(id)
on delete cascade;