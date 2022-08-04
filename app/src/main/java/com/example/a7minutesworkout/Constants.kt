package com.example.a7minutesworkout

object Constants {
    fun defaultExerciseList() : ArrayList<ExerciseModel>{
        val exerciseList = ArrayList<ExerciseModel>()

        val abdominalCrunch = ExerciseModel(
            1,
            "Abdominal Crunch",
            R.drawable.ic_abdominal_crunch,
            false,
            false
        )
        val highKnees = ExerciseModel(
            2,
            "High Knees Running in place",
            R.drawable.ic_high_knees_running_in_place,
            false,
            false
        )
        val jumpingJacks = ExerciseModel(
            3,
            "Jumping Jacks",
            R.drawable.ic_jumping_jacks,
            false,
            false
        )
        val lunge = ExerciseModel(
            4,
            "Lunge",
            R.drawable.ic_lunge,
            false,
            false
        )
        val plank = ExerciseModel(
            5,
            "Plank",
            R.drawable.ic_plank,
            false,
            false
        )
        val pushUp = ExerciseModel(
            6,
            "Push Up",
            R.drawable.ic_push_up,
            false,
            false
        )
        val pushUpAndRotation = ExerciseModel(
            7,
            "Push up & Rotation",
            R.drawable.ic_push_up_and_rotation,
            false,
            false
        )
        val sidePlank = ExerciseModel(
            8,
            "Side Plank",
            R.drawable.ic_side_plank,
            false,
            false
        )
        val squat = ExerciseModel(
            9,
            "Squat",
            R.drawable.ic_squat,
            false,
            false
        )
        val stepUpChair = ExerciseModel(
            10,
            "Step Up Onto Chair",
            R.drawable.ic_step_up_onto_chair,
            false,
            false
        )
        val tricepsDipChair = ExerciseModel(
            11,
            "Triceps Dip on Chair",
            R.drawable.ic_triceps_dip_on_chair,
            false,
            false
        )
        val wallSit = ExerciseModel(
            12,
            "Wall Sit",
            R.drawable.ic_wall_sit,
            false,
            false
        )

        // Adding to the list
        exerciseList.add(abdominalCrunch)
        exerciseList.add(highKnees)
        exerciseList.add(jumpingJacks)
        exerciseList.add(lunge)
        exerciseList.add(plank)
        exerciseList.add(pushUp)
        exerciseList.add(pushUpAndRotation)
        exerciseList.add(sidePlank)
        exerciseList.add(squat)
        exerciseList.add(tricepsDipChair)
        exerciseList.add(wallSit)

        return exerciseList
    }
}