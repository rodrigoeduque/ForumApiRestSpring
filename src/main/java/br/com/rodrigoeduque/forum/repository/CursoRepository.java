package br.com.rodrigoeduque.forum.repository;

import br.com.rodrigoeduque.forum.modelo.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso,Long> {
    Curso findByNome(String nomeCurso);
}
