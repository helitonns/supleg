ALTER TABLE "sup_leg".roteiro ADD COLUMN roteiro_presidente_revisado boolean DEFAULT false;
ALTER TABLE "sup_leg".roteiro ADD COLUMN roteiro_secretario_revisado boolean DEFAULT false;

UPDATE "sup_leg".roteiro SET roteiro_presidente_revisado=true, roteiro_secretario_revisado=true;