package com.luisdam.projectecafeterialuisc.data.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.luisdam.projectecafeterialuisc.model.Comanda
import com.luisdam.projectecafeterialuisc.model.ProducteComanda
import kotlinx.coroutines.tasks.await
import java.util.Date

class FirestoreService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val comandesCollection = "comandes"

    suspend fun guardarComanda(comanda: Comanda): Result<String> {
        return try {
            val comandaData = hashMapOf(
                "usuariId" to comanda.usuariId,
                "usuariNom" to comanda.usuariNom,
                "total" to comanda.total,
                "data" to comanda.data,
                "estat" to comanda.estat,
                "productes" to comanda.productes.map { producte ->
                    hashMapOf(
                        "nom" to producte.nom,
                        "preu" to producte.preu,
                        "quantitat" to producte.quantitat
                    )
                }
            )

            val documentRef = db.collection(comandesCollection).add(comandaData).await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenirComandesUsuari(usuariId: String): Result<List<Comanda>> {
        return try {
            val querySnapshot = db.collection(comandesCollection)
                .whereEqualTo("usuariId", usuariId)
                .orderBy("data", Query.Direction.DESCENDING)
                .get()
                .await()

            val comandes = querySnapshot.documents.map { document ->
                Comanda(
                    id = document.id,
                    usuariId = document.getString("usuariId") ?: "",
                    usuariNom = document.getString("usuariNom") ?: "",
                    total = document.getDouble("total") ?: 0.0,
                    data = document.getDate("data") ?: Date(),
                    estat = document.getString("estat") ?: "Pendent",
                    productes = (document.get("productes") as? List<HashMap<String, Any>>)?.map {
                        ProducteComanda(
                            nom = it["nom"] as? String ?: "",
                            preu = it["preu"] as? Double ?: 0.0,
                            quantitat = it["quantitat"] as? Int ?: 1
                        )
                    } ?: emptyList()
                )
            }

            Result.success(comandes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualitzarComanda(comandaId: String, total: Double? = null, estat: String? = null): Result<Unit> {
        return try {
            val updates = hashMapOf<String, Any>()
            total?.let { updates["total"] = it }
            estat?.let { updates["estat"] = it }

            db.collection(comandesCollection)
                .document(comandaId)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarComanda(comandaId: String): Result<Unit> {
        return try {
            db.collection(comandesCollection)
                .document(comandaId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}