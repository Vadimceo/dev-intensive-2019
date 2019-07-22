package ru.skillbranch.devintensive.models

class Bender(var status:Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion():String = when(question){
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question

    }

    fun listenAnswer(answer:String):Pair<String, Triple<Int, Int, Int>>{

        return validateAnswer(answer) to status.color
    }

    fun validateAnswer(answer: String): String = if (question.validate(answer).equals("Отлично - ты справился")) {
        if (question.answer.contains(answer.toLowerCase())) {
            question = question.nextQuestion()
            "Отлично - ты справился\n${question.question}"
        } else if (status == Status.CRITICAL) {
            status = Status.NORMAL
            "Это неправильный ответ. Давай все по новой\n${Question.NAME.question}"
        } else {
            status = status.nextStatus()
            "Это неправильный ответ\n${question.question}"
        }
    } else {
        "${question.validate(answer)}\n${question.question}"
    }



    enum class Status(val color: Triple<Int, Int, Int>){
        NORMAL(Triple(255,255,255)),
        WARNING(Triple(255,120,0)),
        DANGER(Triple(255,60,60)),
        CRITICAL(Triple(255,0,0));

        fun nextStatus():Status{
            return if(this.ordinal<values().lastIndex){
                values()[this.ordinal+1]
            }else{
                values()[0]
            }
        }
    }
    enum class Question(val question: String, val answer: List<String>){
        NAME("Как меня зовут?", listOf("бендер","bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun validate(answer: String): String =
                if(answer.trim().firstOrNull()?.isUpperCase()?:false){"Отлично - ты справился"}
                else {"Имя должно начинаться с заглавной буквы"}
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик","bender")){
            override fun nextQuestion(): Question = MATERIAL
            override fun validate(answer: String): String =
                if(!(answer.trim().firstOrNull()?.isUpperCase()?: false)){"Отлично - ты справился"}
                else {"Профессия должна начинаться со строчной буквы"}
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron","wood")){
            override fun nextQuestion(): Question = BDAY
            override fun validate(answer: String): String =
                if(answer.trim().matches(Regex("^\\D*$"))){"Отлично - ты справился"}
                else {"Материал не должен содержать цифр"}
        },
        BDAY("Когда меня создали?", listOf("2993")){
            override fun nextQuestion(): Question = SERIAL
            override fun validate(answer: String): String =
                if(answer.trim().matches(Regex("^[0-9]*$"))){"Отлично - ты справился"}
                else {"Год моего рождения должен содержать только цифры"}
        },
        SERIAL("Мой серийный номер?", listOf("2716057")){
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): String =
                if(answer.trim().matches(Regex("^[0-9]*$")) && answer.length == 7) {"Отлично - ты справился"}
                else {"Серийный номер содержит только цифры, и их 7"}
        },
        IDLE("На этом все, вопросов больше нет", listOf("бендер","bender")){
            override fun nextQuestion(): Question = IDLE
            override fun validate(answer: String): String = ""
        };

        abstract fun nextQuestion():Question
        abstract fun validate(answer: String): String
    }
}
